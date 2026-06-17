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
import com.example.apoiodigital.feature.screen_question.CarrosselService;
import com.example.apoiodigital.feature.screen_question.InputService;
import com.example.apoiodigital.feature.screen_question.QuestionView;
import com.example.apoiodigital.feature.screen_question.UserAnswerValidatorRequestDTO;
import com.example.apoiodigital.feature.tutorial.TutorialViewModel;
import com.example.apoiodigital.feature.tutorial.data.BaseDataToIADTO;

import java.util.ArrayList;
import java.util.List;

public class OverlayService extends LifecycleService {

    private WindowManager windowManager;
    private OverlayLayoutBinding overlayLayoutBinding;

    private View mainOverlay;

    private final List<Atalho> atalhosCache = new ArrayList<>();

    private OverlayViewManager viewManager;


    @Override
    public void onCreate() {
        super.onCreate();
        criarNotificacaoForeground();

        WindowManagerUtils windowManagerUtils = new WindowManagerUtils();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        WindowManager.LayoutParams params = windowManagerUtils.getWindowParams();

        ContextThemeWrapper themeWrapper = new ContextThemeWrapper(this, R.style.Theme_ApoioDigital_MakingModal);

        LayoutInflater layoutInflater = LayoutInflater.from(themeWrapper);

        mainOverlay = layoutInflater.inflate(R.layout.overlay_layout, null);
        windowManager.addView(mainOverlay, params);

        overlayLayoutBinding = OverlayLayoutBinding.bind(mainOverlay);

        viewManager = new OverlayViewManager(this);

        OverlayViewModel overlayViewModel = new OverlayViewModel(this, overlayLayoutBinding, viewManager);
        overlayViewModel.setupObservers(mainOverlay, atalhosCache, this, new OverlayViewModel.OverlayListener() {
            @Override
            public void onAbrirApp(Requisicao requisicao, TutorialViewModel tutorialViewModel) {
                        String contexto = abrirAplicativoERetornarContexto(requisicao);

                        BaseDataToIADTO baseDataToIADTO = new BaseDataToIADTO(requisicao.getPrompt(), contexto, requisicao.getId());

                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            tutorialViewModel.getBaseDataToIA().postValue(baseDataToIADTO);
                        }, 4000);

//                        enviarDadosParaIA(requisicao.getPrompt(), requisicao.getId(), contexto);
                };

            @Override
            public void onMostrarTutorialView() {
                viewManager.showTutorialView(mainOverlay, overlayLayoutBinding, new OverlayViewManager.TutorialCallBack() {
                    @Override
                    public void onCloseButtonClicked() {
                        stopSelf();
                    }
                });
            }
        });

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


    private String abrirAplicativoERetornarContexto(Requisicao requisicao){
        String pacoteApp = requisicao.getAppSuportado().getPacote();
        String nomeApp = requisicao.getAppSuportado().getNome();

        OpenAppService openAppService = new OpenAppService();
        return openAppService.openAppByPacoteAndReturnsContexto(this, pacoteApp, nomeApp);

    }


}
