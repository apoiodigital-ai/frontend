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

import java.util.List;

public class OverlayViewModel extends ViewModel {

    private RequisicaoController requisicaoController;
    private AtalhoController atalhoController;
    private UsuarioController usuarioController;
    private AnswerValidatorController answerValidatorController;
    private TutorialViewModel tutorialViewModel;


    private final OverlayLayoutBinding overlayLayoutBinding;

    private final OverlayViewManager viewManager;

    private ModalView modalView;

    public OverlayViewModel(Context context, OverlayLayoutBinding overlayLayoutBinding) {

        this.overlayLayoutBinding = overlayLayoutBinding;
        this.viewManager = new OverlayViewManager(context);

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
        tutorialViewModel.getChecksInformationNeedsResponse().observeForever(response -> {
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
                    overlayListener.onEnviarDadosAdicionais(pergunta, resposta);
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

        answerValidatorController.getResponseData().observeForever(response -> {
            if(response.isSatisfaz()){
                overlayListener.onEnviarDadosAdicionais(response.getPergunta_especificacao(), response.getResposta_especificacao());
//                enviarDadosAdicionaisParaIA(response.getPergunta_especificacao(), response.getResposta_especificacao());
            }
        });

        requisicaoController.getState().observeForever(state -> {
            modalView.setModalLoading(state.isLoading());
        });

        requisicaoController.getRequisicaoResponse().observeForever(resp -> {
//            String contexto = abrirAplicativoERetornarContexto(resp.getRequisicao());
//
//            enviarDadosParaIA(resp.getRequisicao().getPrompt(), resp.getRequisicao().getId(), contexto);

            overlayListener.onAbrirApp(resp.getRequisicao());

        });

        requisicaoController.getState().observe(owner, state -> {
            if(state.isSuccess()){

                viewManager.showTutorialView(mainOverlay, overlayLayoutBinding);
            }
        });

        atalhoController.getInitAtalhoState().observe(owner, state -> {
            if(state.isSuccess()){


                viewManager.showTutorialView(mainOverlay, overlayLayoutBinding);
            }

        });


        atalhoController.getAtalhoResponse().observeForever(requisicao -> {

//            String contexto = abrirAplicativoERetornarContexto(requisicao);
//
//            enviarDadosParaIA(requisicao.getPrompt(), requisicao.getId(), contexto);

            overlayListener.onAbrirApp(requisicao);

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
            this.modalView = iniciarFluxoModal(userID, viewManager, atalhosCache);
            atalhoController.carregarAtalhos(userID);
            modalView.setModalSettings();


        });
    }


    public interface OverlayListener{
        void onAbrirApp(Requisicao requisicao);
        void onEnviarDadosAdicionais(String pergunta, String resposta);

    }

}
