package com.example.apoiodigital.AccessibilityService;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.example.apoiodigital.Model.Bounds;
import com.example.apoiodigital.Model.CryptedRequestIA;
import com.example.apoiodigital.Model.CryptedResponseIA;
import com.example.apoiodigital.Model.DescryptedRequestIA;
import com.example.apoiodigital.Model.DescryptedResponseIA;
import com.example.apoiodigital.Model.UiComponent;
import com.example.apoiodigital.Service.CryptService;
import com.example.apoiodigital.ViewModel.tutorial.TutorialViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public class GetElementService extends AccessibilityService {

    private final List<UiComponent> components = new ArrayList<>();
    private final TutorialViewModel service = new TutorialViewModel(this);
    private AccessibilityNodeInfo chosedComponent;
    private List<AccessibilityNodeInfo> nodesSent = new ArrayList<>();
    private int idComponent = 0;
    private static final String TAG = "GetElementService";
    private String requisicaoCache;
    private String requisicaoIdCache;
    private String contextoCache = "";

    private static final String APOIODIGITAL_PACKAGE = "com.example.apoiodigital_makingmodal"; // seu pacote aqui


    private final BroadcastReceiver receiverSendComponents = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            var gson = new Gson();

            getViewEvent();

            String requisicao = intent.getStringExtra("prompt");
            String id_requisicao = intent.getStringExtra("id_requisicao");
            DescryptedRequestIA descryptedRequestIA = new DescryptedRequestIA();
            descryptedRequestIA.setElementos(components);
            descryptedRequestIA.setContexto(contextoCache);
            if(requisicao != null){
                requisicaoCache = requisicao;
            }
            if(id_requisicao != null){
                requisicaoIdCache = id_requisicao;
            }

            descryptedRequestIA.setPergunta(requisicaoCache);


            String descryptedJson = gson.toJson(descryptedRequestIA);

            CryptService cryptService = new CryptService();
            String publicKey = cryptService.getPublicKey();
            String cryptedJson = cryptService.encripty(descryptedJson, publicKey);

            CryptedRequestIA cryptedRequestIA = new CryptedRequestIA();
            cryptedRequestIA.setTextCrypted(cryptedJson);
            cryptedRequestIA.setKey(publicKey);
            cryptedRequestIA.setId_requisicao(requisicaoIdCache);

            Log.e(TAG, "onReceive: " + id_requisicao);
            Log.e(TAG, "onReceive: " + cryptedRequestIA);

            service.getResponseIA(cryptedRequestIA);
        }
    };

    private final BroadcastReceiver receiverResponseIA = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            String response = intent.getStringExtra("responseIA");
            Log.e("CryptServiceLOGE", "onReceive: " + response );

            CryptedResponseIA responseIA = gson.fromJson(response, CryptedResponseIA.class);

            CryptService cryptService = new CryptService();
            var json = cryptService.descrypt(responseIA.getIaMessage(), responseIA.getKey());
            Log.e("CryptServiceLOGE", "onReceiveDesc1: " + json );

            DescryptedResponseIA descryptedResponseIA = gson.fromJson(json, DescryptedResponseIA.class);
            contextoCache+=descryptedResponseIA.getContext();
            if( descryptedResponseIA.getViewID() == null) return;

            chosedComponent = nodesSent.get(descryptedResponseIA.getViewID()-1);
            var rect = new Rect();
            chosedComponent.getBoundsInScreen(rect);
            Bounds bounds = new Bounds(rect.left, rect.right, rect.top, rect.bottom);

            String boundsJson = gson.toJson(bounds);
            Log.e("CryptServiceLOGE", "onReceiveDesc2: " + boundsJson );

            var intentMask = new Intent("com.example.apoiodigital.SET_MASK_VIEW");
            intentMask.putExtra("chosedComponentBounds", boundsJson);
            intentMask.putExtra("messageIA", descryptedResponseIA.getMessageIA());
            sendBroadcast(intentMask);
        }};

    private final BroadcastReceiver clickReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "onReceive: Recebeu");

            if(chosedComponent.isClickable()){
                chosedComponent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }

            try {
                TimeUnit.SECONDS.sleep(3);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            var intentSendToIA = new Intent("com.example.apoiodigital.SEND_TO_IA");
            intentSendToIA.putExtra("requisicao", requisicaoCache);
            sendBroadcast(intentSendToIA);

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
        var componentModel = new UiComponent(this.idComponent, className, addinfoInString);

        components.add(componentModel);
        nodesSent.add(rootNodes);
        printComponents(componentModel);

    }

    public void printComponents(UiComponent model){
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
        var gson = new Gson();
        components.clear();
        nodesSent.clear();
        idComponent = 0;
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
//        rootNode.findAccessibilityNodeInfosByViewId()

        getComponent(rootNode);

    }


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {


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

        super.onDestroy();


    }
}

