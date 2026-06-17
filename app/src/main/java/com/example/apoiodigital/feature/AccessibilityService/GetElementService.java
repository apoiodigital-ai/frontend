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

import com.example.apoiodigital.feature.tutorial.data.BaseDataToIADTO;
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


    @Override
    public void onCreate() {
        super.onCreate();

        this.tutorialViewModel = new TutorialViewModel(this);

        setupObservers();

    }

    // This function is used to receive the click event from the MaskActivity
    private final BroadcastReceiver clickReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "onReceive: Recebeu");

            if(chosedComponent.isClickable()){
                chosedComponent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                BaseDataToIADTO baseDataToIADTO = new BaseDataToIADTO(promptCache, contextoCache, requisicaoIdCache);
                tutorialViewModel.getBaseDataToIA().postValue(baseDataToIADTO);

            }, 3000); // 3000 milissegundos = 3 segundos

        }
    };

    private void setupObservers(){
        tutorialViewModel.getAdditionalInfo().observeForever(dto ->
        {

            FindBestAnswerRequestDTO requestDTO = new FindBestAnswerRequestDTO();

            requestDTO.setElementos(components);
            requestDTO.setContexto(contextoCache);
            requestDTO.setPrompt(promptCache);
            requestDTO.setPergunta_especificacao(dto.getPergunta());
            requestDTO.setResposta_especificacao(dto.getResposta());
            tutorialViewModel.getResponseIA(requestDTO);

        });

        tutorialViewModel.getBaseDataToIA().observeForever(dto -> {
            getViewEvent();

            String prompt = dto.getPrompt();
            String id_requisicao = dto.getId_requisicao();
            String contexto = dto.getContexto();

            if(prompt != null){
                promptCache = prompt;
            }
            if(id_requisicao != null){
                requisicaoIdCache = id_requisicao;
            }
            if(contexto != null) {
                contextoCache = contexto;
            }
            Log.e(TAG, "setupObservers: " + contexto);
            Log.e(TAG, "setupObservers: " + contextoCache);

            // ----
            ChecksInformationNeedsRequestDTO checksInformationNeedsRequestDTO =
                    new ChecksInformationNeedsRequestDTO(components, contextoCache, promptCache);

            tutorialViewModel.checkInformationNeeds(checksInformationNeedsRequestDTO);
            // ----
        });

        tutorialViewModel.getChecksInformationNeedsResponse().observeForever(response -> {
            if(response != null) return;

            FindBestAnswerRequestDTO requestDTO = new FindBestAnswerRequestDTO();

            requestDTO.setElementos(components);
            requestDTO.setContexto(contextoCache);
            requestDTO.setPrompt(promptCache);
            tutorialViewModel.getResponseIA(requestDTO);

        });

        tutorialViewModel.getAnswerResponse().observeForever(responseIA -> {

            Gson gson = new Gson();

            if( responseIA.getViewID() == null) return;

            chosedComponent = nodesSent.get(responseIA.getViewID()-1);

            this.contextoCache = responseIA.getNovo_contexto();

            var rect = new Rect();
            chosedComponent.getBoundsInScreen(rect);
            Bounds bounds = new Bounds(rect.left, rect.right, rect.top, rect.bottom);

            String boundsJson = gson.toJson(bounds);

            var intentMask = new Intent("com.example.apoiodigital.SET_MASK_VIEW");
            intentMask.putExtra("chosedComponentBounds", boundsJson);
            intentMask.putExtra("messageIA", responseIA.getMensagem_escrita());
            sendBroadcast(intentMask);
        });
    }

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

        IntentFilter intentFilter3 = new IntentFilter();
        intentFilter3.addAction("com.example.apoiodigital.CLICK_ELEMENT");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(clickReceiver, intentFilter3, RECEIVER_EXPORTED);
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

        unregisterReceiver(clickReceiver);


        super.onDestroy();


    }
}

