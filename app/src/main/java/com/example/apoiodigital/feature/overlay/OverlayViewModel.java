package com.example.apoiodigital.feature.overlay;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

import com.example.apoiodigital.core.tables.atalho.Atalho;
import com.example.apoiodigital.core.tables.requisicao.Requisicao;
import com.example.apoiodigital.core.tables.usuario.UsuarioController;
import com.example.apoiodigital.databinding.OverlayLayoutBinding;
import com.example.apoiodigital.feature.modal.ModalView;
import com.example.apoiodigital.feature.modal.data.RequisicaoInput;
import com.example.apoiodigital.feature.modal.viewmodel.AtalhoController;
import com.example.apoiodigital.feature.modal.viewmodel.RequisicaoController;
import com.example.apoiodigital.feature.screen_question.AnswerValidatorController;
import com.example.apoiodigital.feature.screen_question.CarrosselService;
import com.example.apoiodigital.feature.screen_question.InputService;
import com.example.apoiodigital.feature.screen_question.QuestionView;
import com.example.apoiodigital.feature.screen_question.UserAnswerValidatorRequestDTO;
import com.example.apoiodigital.feature.tutorial.TutorialViewModel;
import com.example.apoiodigital.feature.tutorial.data.AdditionalInfoDTO;

import java.util.List;

public class OverlayViewModel extends ViewModel {

    private final RequisicaoController requisicaoController;
    private final AtalhoController atalhoController;
    private final UsuarioController usuarioController;
    private final AnswerValidatorController answerValidatorController;
    private final TutorialViewModel tutorialViewModel;

    private final OverlayLayoutBinding overlayLayoutBinding;

    private final OverlayViewManager viewManager;

    private ModalView modalView;

    public OverlayViewModel(Context context, OverlayLayoutBinding overlayLayoutBinding, OverlayViewManager viewManager) {

        this.overlayLayoutBinding = overlayLayoutBinding;
        this.viewManager = viewManager;

        requisicaoController = new RequisicaoController();
        atalhoController = new AtalhoController();
        usuarioController = new UsuarioController();
        answerValidatorController = new AnswerValidatorController();
        tutorialViewModel = new TutorialViewModel(context);

        usuarioController.getIdByToken(context);

    }

    public ModalView iniciarFluxoModal(String userID, OverlayViewManager viewManager, List<Atalho> atalhos){
        viewManager.showModalView(new ModalView.ModalListener() {
            @Override
            public void onPromptSent(String prompt) {
                var request = new RequisicaoInput(prompt, userID);
                requisicaoController.enviarRequisicao(request);
            }

            @Override
            public void atalhoInit(int index) {
                atalhoController.iniciarAtalho(atalhos.get(index).getId());
            }
        }, overlayLayoutBinding);

        return (ModalView) viewManager.getCurrentView();
    }

    public void setupObservers(View mainOverlay, List<Atalho> atalhosCache, LifecycleOwner owner, OverlayListener overlayListener){
        tutorialViewModel.getChecksInformationNeedsResponse().observe(owner, response -> {
            if(response == null) return;

            Log.e("OVERLAYSERVICE", "setupObservers: " + response.getDescricao_duvida());

            viewManager.showQuestionView(mainOverlay, overlayLayoutBinding);
            QuestionView questionView = (QuestionView) viewManager.getCurrentView();
            questionView.setQuestion(response.getPergunta());
            questionView.setCarrossel(response.getOpcoes());

            questionView.setButtonClick(new CarrosselService.ButtonListener() {
                @Override
                public void onClick(String pergunta, String resposta) {
                    //enviarDadosAdicionaisParaIA(pergunta, resposta);
                    AdditionalInfoDTO dto = new AdditionalInfoDTO(pergunta, resposta);
//                    overlayListener.onEnviarDadosAdicionais(dto);
                    tutorialViewModel.getAdditionalInfo().postValue(dto);
                    overlayListener.onMostrarTutorialView();
                }
            });

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

        answerValidatorController.getResponseData().observe(owner,response -> {
            if(response.isSatisfaz()){
                AdditionalInfoDTO dto = new AdditionalInfoDTO(response.getPergunta_especificacao(), response.getResposta_especificacao());
                tutorialViewModel.getAdditionalInfo().postValue(dto);
                overlayListener.onMostrarTutorialView();
//                overlayListener.onEnviarDadosAdicionais(dto);
            }
        });

        requisicaoController.getState().observe(owner, state -> {
            modalView.setModalLoading(state.isLoading());
        });

        requisicaoController.getRequisicaoResponse().observe(owner, resp -> {

            overlayListener.onAbrirApp(resp.getRequisicao(), this.tutorialViewModel);

        });

        requisicaoController.getState().observe(owner, state -> {
            if(state.isSuccess()){

                overlayListener.onMostrarTutorialView();
//                viewManager.showTutorialView(mainOverlay, overlayLayoutBinding);
            }
        });

        atalhoController.getInitAtalhoState().observe(owner, state -> {
            if(state.isSuccess()){

                overlayListener.onMostrarTutorialView();

            }

        });


        atalhoController.getAtalhoResponse().observe(owner, requisicao -> {

            overlayListener.onAbrirApp(requisicao, this.tutorialViewModel);

        });

        atalhoController.getGetAtalhosResponse().observe(owner,atalhos -> {

            if (atalhos.size() >= 3) {
                modalView.setAtalhosText(atalhos);
            }

            atalhosCache.clear();
            atalhosCache.addAll(atalhos);
        });

        atalhoController.getGetAtalhosState().observe(owner,state -> {
            modalView.setAtalhoLoading(state.isLoading());
        });

        usuarioController.getUserID().observe(owner, userIDDTO -> {

            if (userIDDTO == null || userIDDTO.getUserID() == null) {
                Log.e("ModalView", "Erro: API retornou userIDDTO nulo!");
                return;
            }

            String userID = userIDDTO.getUserID().toString();
            this.modalView = iniciarFluxoModal(userID, viewManager, atalhosCache);
            atalhoController.carregarAtalhos(userID);
            modalView.setModalSettings();


        });
    }


    public interface OverlayListener{
        void onAbrirApp(Requisicao requisicao, TutorialViewModel tutorialViewModel);

        void onMostrarTutorialView();
    }

}
