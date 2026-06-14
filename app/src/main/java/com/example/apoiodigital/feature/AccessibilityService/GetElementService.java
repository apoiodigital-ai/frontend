package com.example.apoiodigital.feature.AccessibilityService;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.example.apoiodigital.feature.tutorial.data.ChecksInformationNeedsRequestDTO;
import com.example.apoiodigital.feature.tutorial.data.FindBestAnswerResponseDTO;
import com.example.apoiodigital.feature.tutorial.data.Bounds;
import com.example.apoiodigital.feature.tutorial.data.FindBestAnswerRequestDTO;
import com.example.apoiodigital.feature.tutorial.data.Componente;
import com.example.apoiodigital.feature.tutorial.TutorialViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public class GetElementService extends AccessibilityService {

    private final List<Componente> components = new ArrayList<>();
    private TutorialViewModel tutorialViewModel;
    private AccessibilityNodeInfo chosedComponent;
    private List<AccessibilityNodeInfo> nodesSent = new ArrayList<>();
    private int idComponent = 0;
    private static final String TAG = "GetElementService";
    private String promptCache;
    private String requisicaoIdCache;
    private String contextoCache = "";


    // This function is used to send Components to IA
    private final BroadcastReceiver receiverSendComponents = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            getViewEvent();

            String prompt = intent.getStringExtra("prompt");
            String id_requisicao = intent.getStringExtra("id_requisicao");
            String contexto = intent.getStringExtra("contexto");


            if(prompt != null){
                promptCache = prompt;
            }
            if(id_requisicao != null){
                requisicaoIdCache = id_requisicao;
            }
            if(contexto != null) {
                contextoCache = contexto;
            }

            // ----
            ChecksInformationNeedsRequestDTO checksInformationNeedsRequestDTO =
                    new ChecksInformationNeedsRequestDTO(components, contextoCache, promptCache);

            tutorialViewModel.checkInfomationNeeds(checksInformationNeedsRequestDTO);
            // ----


        }
    };


    @Override
    public void onCreate() {
        super.onCreate();

        this.tutorialViewModel = new TutorialViewModel(this);

        tutorialViewModel.getChecksInformationNeedsResponse().observeForever(response -> {
            if(response != null) return;

            FindBestAnswerRequestDTO requestDTO = new FindBestAnswerRequestDTO();

            requestDTO.setElementos(components);
            requestDTO.setContexto(contextoCache);
            requestDTO.setPrompt(promptCache);
            tutorialViewModel.getResponseIA(requestDTO);

        });

    }

    private final BroadcastReceiver sendToIAWithAddittionalInfo = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            FindBestAnswerRequestDTO requestDTO = new FindBestAnswerRequestDTO();

            requestDTO.setElementos(components);
            requestDTO.setContexto(contextoCache);
            requestDTO.setPrompt(promptCache);
            requestDTO.setPergunta_especificacao(intent.getStringExtra("pergunta_espc"));
            requestDTO.setResposta_especificacao(intent.getStringExtra("resposta_espc"));

            tutorialViewModel.getResponseIA(requestDTO);
        }
    };

    // This function is used to receive the response from the IA and to send other broadcast to MaskActivity
    private final BroadcastReceiver receiverResponseIA = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            String response = intent.getStringExtra("responseIA");

            FindBestAnswerResponseDTO responseIA = gson.fromJson(response, FindBestAnswerResponseDTO.class);

            if( responseIA.getViewID() == null) return;

            chosedComponent = nodesSent.get(responseIA.getViewID()-1);

            contextoCache = responseIA.getNovo_contexto();

            var rect = new Rect();
            chosedComponent.getBoundsInScreen(rect);
            Bounds bounds = new Bounds(rect.left, rect.right, rect.top, rect.bottom);

            String boundsJson = gson.toJson(bounds);

            var intentMask = new Intent("com.example.apoiodigital.SET_MASK_VIEW");
            intentMask.putExtra("chosedComponentBounds", boundsJson);
            intentMask.putExtra("messageIA", responseIA.getMensagem_escrita());
            sendBroadcast(intentMask);
        }};

    // This function is used to receive the click event from the MaskActivity
    private final BroadcastReceiver clickReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "onReceive: Recebeu");

            if(chosedComponent.isClickable()){
                chosedComponent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                var intentSendToIA = new Intent("com.example.apoiodigital.SEND_TO_IA");
                intentSendToIA.putExtra("requisicao", promptCache);
                intentSendToIA.putExtra("contexto", contextoCache);
                sendBroadcast(intentSendToIA);

            }, 3000); // 3000 milissegundos = 3 segundos



        }
    };

    public boolean filterComponents(AccessibilityNodeInfo node){
        if(!node.isVisibleToUser()) return false;
        if(!node.isClickable()) return false;

        if(node.getClassName().toString().equals("android.widget.ImageView") && node.getContentDescription() == null)
            return false;

        if(node.getClassName().toString().equals("android.widget.TextView") && node.getText() == null)
            return false;

        return true;
    }

    public List<String> setAddicionalInfo(AccessibilityNodeInfo node){

        var addicionalInfo = new ArrayList<String>();

        if(node == null) return null;
        if(node.getText() != null){
            addicionalInfo.add(node.getText().toString());
        }
        if(node.getContentDescription() != null){
            addicionalInfo.add(node.getContentDescription().toString());
        }

        for(int i = 0; i < node.getChildCount(); i++){
            addicionalInfo.addAll(setAddicionalInfo(node.getChild(i)));
        }

        return addicionalInfo;

    }

    public void addComponent(AccessibilityNodeInfo rootNodes){

        var className = rootNodes.getClassName().toString();

        var addinfo = setAddicionalInfo(rootNodes);
        String addinfoInString = addinfo.stream()
                .collect(Collectors.joining(" - ", "", ""));

        this.idComponent = this.idComponent+1;

//        Log.e(TAG, "addComponent: " + id);
        var componentModel = new Componente(this.idComponent, className, addinfoInString);

        components.add(componentModel);
        nodesSent.add(rootNodes);
        printComponents(componentModel);

    }

    public void printComponents(Componente model){
        Gson json = new Gson();

        Log.e(TAG, json.toJson(model));

    }

    public void getComponent(AccessibilityNodeInfo node){

        if(node == null) {
            Log.e(TAG, "NULO!");
            return;
        }
//
//        CharSequence pkg = node.getPackageName();
//        if (pkg != null && pkg.toString().equals(APOIODIGITAL_PACKAGE)) {
//            return;
//        }

        if(filterComponents(node)) {
//            Log.e(TAG, "getComponent: ");

            addComponent(node);
        }

        for(int i = 0; i < node.getChildCount(); i++){


            getComponent(node.getChild(i));

        }
    }


    public void getViewEvent(){

        components.clear();
        nodesSent.clear();
        idComponent = 0;
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();

        getComponent(rootNode);

    }


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if(event.getEventTime() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED){
            getViewEvent();
        }

    }


    @Override
    protected void onServiceConnected() {

        super.onServiceConnected();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.apoiodigital.SEND_TO_IA");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(receiverSendComponents, intentFilter, RECEIVER_EXPORTED);
        }

        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("com.example.apoiodigital.GET_RESPONSE_IA");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(receiverResponseIA, intentFilter2, RECEIVER_EXPORTED);
        }

        IntentFilter intentFilter3 = new IntentFilter();
        intentFilter3.addAction("com.example.apoiodigital.CLICK_ELEMENT");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(clickReceiver, intentFilter3, RECEIVER_EXPORTED);
        }

        IntentFilter intentFilter4 = new IntentFilter();
        intentFilter3.addAction("com.example.apoiodigital.SEND_TO_AI_WITH_ADDITIONAL_INFO");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(sendToIAWithAddittionalInfo, intentFilter3, RECEIVER_EXPORTED);
        }
//        getViewEvent();

        var info = new AccessibilityServiceInfo();
        info.flags |= AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;

        this.setServiceInfo(info);
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiverSendComponents);
        unregisterReceiver(clickReceiver);
        unregisterReceiver(receiverResponseIA);
        unregisterReceiver(sendToIAWithAddittionalInfo);

        super.onDestroy();


    }
}

