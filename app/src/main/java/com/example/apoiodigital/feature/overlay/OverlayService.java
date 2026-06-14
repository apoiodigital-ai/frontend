package com.example.apoiodigital.feature.overlay;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.ContextThemeWrapper;
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
import com.example.apoiodigital.feature.modal.ModalView;
import com.example.apoiodigital.feature.modal.data.RequisicaoInput;
import com.example.apoiodigital.feature.modal.service.OpenAppService;
import com.example.apoiodigital.feature.modal.viewmodel.AtalhoController;
import com.example.apoiodigital.feature.modal.viewmodel.RequisicaoController;
import com.example.apoiodigital.feature.screen_question.AnswerValidatorController;
import com.example.apoiodigital.feature.screen_question.InputService;
import com.example.apoiodigital.feature.screen_question.QuestionView;
import com.example.apoiodigital.feature.screen_question.UserAnswerValidatorRequestDTO;
import com.example.apoiodigital.feature.tutorial.TutorialViewModel;

import java.util.ArrayList;
import java.util.List;

public class OverlayService extends LifecycleService {

    private RequisicaoController requisicaoController;
    private AtalhoController atalhoController;
    private UsuarioController usuarioController;
    private AnswerValidatorController answerValidatorController;

    private TutorialViewModel tutorialViewModel;
    private WindowManager windowManager;
    private OverlayLayoutBinding overlayLayoutBinding;

    private ModalView modalView;
    private View mainOverlay;

    private final List<Atalho> atalhosCache = new ArrayList<>();

    private OverlayViewManager viewManager;


    @Override
    public void onCreate() {
        super.onCreate();
        criarNotificacaoForeground();

        requisicaoController = new RequisicaoController();
        atalhoController = new AtalhoController();
        usuarioController = new UsuarioController();
        tutorialViewModel = new TutorialViewModel(this);
        answerValidatorController = new AnswerValidatorController();

        usuarioController.getIdByToken(this);

        WindowManagerUtils windowManagerUtils = new WindowManagerUtils();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        WindowManager.LayoutParams params = windowManagerUtils.getWindowParams();

        ContextThemeWrapper themeWrapper = new ContextThemeWrapper(this, R.style.Theme_ApoioDigital_MakingModal);

        LayoutInflater layoutInflater = LayoutInflater.from(themeWrapper);

        mainOverlay = layoutInflater.inflate(R.layout.overlay_layout, null);
        windowManager.addView(mainOverlay, params);

        overlayLayoutBinding = OverlayLayoutBinding.bind(mainOverlay);

        viewManager = new OverlayViewManager(this);

        setupObservers();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(windowManager != null && mainOverlay != null){
            windowManager.removeView(mainOverlay);
        }
    }

    public void iniciarFluxoModal(String userID){
        viewManager.showModalView(new ModalView.ModalListener() {
            @Override
            public void onPromptSent(String prompt) {
                var request = new RequisicaoInput(prompt, userID);
                requisicaoController.enviarRequisicao(request);
            }

            @Override
            public void atalhoInit(int index) {
                atalhoController.iniciarAtalho(atalhosCache.get(index).getId());
            }
        }, overlayLayoutBinding);

        modalView = (ModalView) viewManager.getCurrentView();
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
        tutorialViewModel.getChecksInformationNeedsResponse().observeForever(response -> {
            if(response == null) return;

            Log.e("OVERLAYSERVICE", "setupObservers: " + response.getDescricao_duvida());

            viewManager.showQuestionView(mainOverlay, overlayLayoutBinding);
            QuestionView questionView = (QuestionView) viewManager.getCurrentView();
            questionView.setQuestion(response.getPergunta());
            questionView.setCarrossel(response.getOpcoes());
            questionView.setInput(new InputService.InputListener() {
                @Override
                public void onPromptButtonClick(String pergunta, String resposta) {
                    Log.e("OVERLAYSERVICE", "onPromptButtonClick: " + resposta);

                    UserAnswerValidatorRequestDTO dto = new UserAnswerValidatorRequestDTO(
                        response.getContexto(),
                       pergunta,
                       resposta,
                       response.getTipo_pendencia(),
                       response.getDescricao_duvida()
               );

                    answerValidatorController.validarRespostaDaNecessidade(dto);
                }
            });
        });

        answerValidatorController.getResponseData().observeForever(response -> {
            if(response.isSatisfaz()){
                Intent i = new Intent("com.example.apoiodigital.SEND_TO_AI_WITH_ADDITIONAL_INFO");
                i.putExtra("pergunta_espc", response.getPergunta_especificacao());
                i.putExtra("resposta_espc", response.getResposta_especificacao());

                sendBroadcast(i);
                viewManager.showTutorialView(mainOverlay, overlayLayoutBinding);
            }
        });

        requisicaoController.getState().observeForever(state -> {
            modalView.setModalLoading(state.isLoading());
        });

        requisicaoController.getRequisicaoResponse().observeForever(resp -> {
            String contexto = abrirAplicativoERetornarContexto(resp.getRequisicao());

            enviarDadosParaIA(resp.getRequisicao().getPrompt(), resp.getRequisicao().getId(), contexto);

        });

        requisicaoController.getState().observe(this, state -> {
            if(state.isSuccess()){

                viewManager.showTutorialView(mainOverlay, overlayLayoutBinding);
            }
        });

        atalhoController.getInitAtalhoState().observe(this, state -> {
            if(state.isSuccess()){


                viewManager.showTutorialView(mainOverlay, overlayLayoutBinding);
            }

        });


        atalhoController.getAtalhoResponse().observeForever(requisicao -> {

            String contexto = abrirAplicativoERetornarContexto(requisicao);

            enviarDadosParaIA(requisicao.getPrompt(), requisicao.getId(), contexto);

        });

        atalhoController.getGetAtalhosResponse().observeForever(atalhos -> {

            if (atalhos.size() >= 3) {
                modalView.setAtalhosText(atalhos);
            }

            atalhosCache.clear();
            atalhosCache.addAll(atalhos);
        });

        atalhoController.getGetAtalhosState().observeForever(state -> {
            modalView.setAtalhoLoading(state.isLoading());
        });

        usuarioController.getUserID().observeForever(userIDDTO -> {

            if (userIDDTO == null || userIDDTO.getUserID() == null) {
                Log.e("ModalView", "Erro: API retornou userIDDTO nulo!");
                return;
            }

            String userID = userIDDTO.getUserID().toString();
            iniciarFluxoModal(userID);
            atalhoController.carregarAtalhos(userID);
            modalView.setModalSettings();


        });
    }

    private void enviarDadosParaIA(String prompt, String id_requisicao, String contexto){

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent i = new Intent("com.example.apoiodigital.SEND_TO_IA");
            i.putExtra("prompt", prompt);
            i.putExtra("id_requisicao", id_requisicao);
            i.putExtra("contexto", contexto);

            sendBroadcast(i);
        }, 4000);

    }

    private String abrirAplicativoERetornarContexto(Requisicao requisicao){
        String pacoteApp = requisicao.getAppSuportado().getPacote();
        String nomeApp = requisicao.getAppSuportado().getNome();

        OpenAppService openAppService = new OpenAppService();
        return openAppService.openAppByPacoteAndReturnsContexto(this, pacoteApp, nomeApp);

    }


}
