package com.example.apoiodigital.feature.overlay;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleService;

import com.example.apoiodigital.R;
import com.example.apoiodigital.core.tables.atalho.Atalho;
import com.example.apoiodigital.core.tables.requisicao.Requisicao;
import com.example.apoiodigital.core.tables.usuario.UsuarioController;
import com.example.apoiodigital.databinding.ModalLayoutBinding;
import com.example.apoiodigital.databinding.OverlayLayoutBinding;
import com.example.apoiodigital.databinding.TuturialLayoutBinding;
import com.example.apoiodigital.feature.modal.ModalView;
import com.example.apoiodigital.feature.modal.service.OpenAppService;
import com.example.apoiodigital.feature.modal.service.TokenService;
import com.example.apoiodigital.feature.modal.viewmodel.AtalhoController;
import com.example.apoiodigital.feature.modal.viewmodel.AudioController;
import com.example.apoiodigital.feature.modal.viewmodel.RequisicaoController;
import com.example.apoiodigital.feature.tutorial.TutorialView;

import java.util.ArrayList;
import java.util.List;

public class OverlayService extends LifecycleService {

    private AudioController audioController;
    private RequisicaoController requisicaoController;
    private AtalhoController atalhoController;
    private UsuarioController usuarioController;

    private WindowManagerService windowManagerService;
    private WindowManager windowManager;
    private ModalLayoutBinding modalBinding;
    private OverlayLayoutBinding overlayLayoutBinding;

    private TuturialLayoutBinding tuturialLayoutBinding;
    private ModalView modalView;
    private TutorialView tutorialView;
    private View mainOverlay;

    private final List<Atalho> atalhosCache = new ArrayList<>();
    private String userID = null;


    @Override
    public void onCreate() {
        super.onCreate();
        criarNotificacaoForeground();

        requisicaoController = new RequisicaoController();
        atalhoController = new AtalhoController();
        audioController = new AudioController();
        usuarioController = new UsuarioController();

        carregarUserID();

        windowManagerService = new WindowManagerService();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        WindowManager.LayoutParams params = windowManagerService.getWindowParams();
        LayoutInflater layoutInflater = LayoutInflater.from(this);

        mainOverlay = layoutInflater.inflate(R.layout.overlay_layout, null);
        windowManager.addView(mainOverlay, params);

        overlayLayoutBinding = OverlayLayoutBinding.bind(mainOverlay);


        modalView = new ModalView(this, layoutInflater, overlayLayoutBinding.container, audioController, atalhoController, requisicaoController);

        overlayLayoutBinding.container.addView(modalView);

        modalBinding = modalView.getBinding();

        setupObservers();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(windowManager != null && mainOverlay != null){
            windowManager.removeView(mainOverlay);
        }
    }

    private void criarNotificacaoForeground() {
        String channelId = "assistente_overlay_channel";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Assistente Digital Ativo", NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }

        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setContentTitle("Assistente Cane Ativo")
                .setContentText("Otimizando a acessibilidade do seu dispositivo.")
                .setSmallIcon(R.drawable.ic_logo_blue) // TODO: Substitua pelo ícone correto
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        startForeground(1, notification);
    }


    private void setupObservers(){
        requisicaoController.getState().observeForever(state -> {
            if (state.isLoading()) {
                modalBinding.modalSubLayout.setVisibility(View.INVISIBLE);
                modalBinding.modalLoading.setVisibility(View.VISIBLE);
                modalBinding.voicePromptInput.setVisibility(View.INVISIBLE);
            } else {
                modalBinding.modalLoading.setVisibility(View.INVISIBLE);
                modalBinding.modalSubLayout.setVisibility(View.VISIBLE);
                modalBinding.voicePromptInput.setVisibility(View.VISIBLE);
            }
        });

        requisicaoController.getRequisicaoResponse().observeForever(resp -> {
            String contexto = abrirAplicativoERetornarContexto(resp.getRequisicao());

            enviarDadosParaIA(resp.getRequisicao().getPrompt(), resp.getRequisicao().getId(), contexto);

        });

        requisicaoController.getState().observe(this, state -> {
            if(state.isSuccess()){
                changeToTutorial();
            }
        });

        atalhoController.getInitAtalhoState().observe(this, state -> {
            if(state.isSuccess()){
                changeToTutorial();
            }
        });


        atalhoController.getAtalhoResponse().observeForever(requisicao -> {

            String contexto = abrirAplicativoERetornarContexto(requisicao);

            enviarDadosParaIA(requisicao.getPrompt(), requisicao.getId(), contexto);

        });

        atalhoController.getGetAtalhosResponse().observeForever(atalhos -> {

            if (atalhos.size() >= 3) {
                modalBinding.sugestoesRapidasBtn.setText(atalhos.get(0).getTitulo());
                modalBinding.sugestoesRapidasBtn2.setText(atalhos.get(1).getTitulo());
                modalBinding.sugestoesRapidasBtn3.setText(atalhos.get(2).getTitulo());
            }

            atalhosCache.clear();
            atalhosCache.addAll(atalhos);
        });

        atalhoController.getGetAtalhosState().observeForever(state -> {
            if (state.isLoading()) {
                modalBinding.sugestoesRapidasSubLayout.setVisibility(View.INVISIBLE);
                modalBinding.sugestoesRapidasLoading.setVisibility(View.VISIBLE);
            } else {
                modalBinding.sugestoesRapidasLoading.setVisibility(View.INVISIBLE);
                modalBinding.sugestoesRapidasSubLayout.setVisibility(View.VISIBLE);
            }
        });

        usuarioController.getUserID().observeForever(userIDDTO -> {

            if (userIDDTO == null || userIDDTO.getUserID() == null) {
                Log.e("ModalView", "Erro: API retornou userIDDTO nulo!");
                return;
            }

            this.userID = userIDDTO.getUserID().toString();
            atalhoController.carregarAtalhos(userID);
            modalView.setModalSettings(userID, atalhosCache);


        });
    }

    private void carregarUserID(){
        TokenService tokenService = new TokenService();
        String token = tokenService.getAccessToken(this);

        if (token == null || token.isEmpty()) {
            Log.e("ModalView", "Token está vazio. Não é possível obter o userID.");
            return;
        }

        usuarioController.getIdByToken(token);

    }

    private void changeToTutorial() {
        try {

            windowManager.removeView(modalView);

        } catch (Exception ignoredEx) {}

        windowManager.updateViewLayout(mainOverlay, windowManagerService.getWindowParamsForTutorial());

        LayoutInflater layoutInflater = LayoutInflater.from(this);

        overlayLayoutBinding.container.removeAllViews();
        tutorialView = new TutorialView(this, layoutInflater, overlayLayoutBinding.container);
        overlayLayoutBinding.container.addView(tutorialView);
    }

    private void enviarDadosParaIA(String prompt, String id_requisicao, String contexto){


        Intent i = new Intent("com.example.apoiodigital.SEND_TO_IA");
        i.putExtra("prompt", prompt);
        i.putExtra("id_requisicao", id_requisicao);
        i.putExtra("contexto", contexto);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            sendBroadcast(i);
        }, 3000); // 3000 milissegundos = 3 segundos


    }

    private String abrirAplicativoERetornarContexto(Requisicao requisicao){
        String pacoteApp = requisicao.getAppSuportado().getPacote();
        String nomeApp = requisicao.getAppSuportado().getNome();

        OpenAppService openAppService = new OpenAppService();
        return openAppService.openAppByPacoteAndReturnsContexto(this, pacoteApp, nomeApp);

    }


}
