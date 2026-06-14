package com.example.apoiodigital.feature.screen_question;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.apoiodigital.databinding.QuestionLayoutBinding;
import com.example.apoiodigital.feature.tutorial.data.TiposPendencia;

public class InputService {

    private final Context context;
    private QuestionLayoutBinding binding;
    private final String contextoTela;
    private final String descricaoDuvida;
    private final TiposPendencia tiposPendencia;

    private final AnswerValidatorController answerValidatorController;
    public InputService(Context context, LayoutInflater inflater, ViewGroup root, AnswerValidatorController answerValidatorController, String contextoTela, String descricao_duvida, TiposPendencia tipo_dependencia) {
        this.context = context;
        this.answerValidatorController = answerValidatorController;

        this.contextoTela = contextoTela;
        this.descricaoDuvida = descricao_duvida;
        this.tiposPendencia = tipo_dependencia;

        init(inflater, root);
        setSendPromptButton();
    }

    private void init(LayoutInflater inflater, ViewGroup root){

        binding = QuestionLayoutBinding.inflate(inflater, root, true);

    }

    public void setSendPromptButton(){
        binding.sendPromptBtn.setOnClickListener(view -> {
           String resposta = binding.promptInput.getText().toString();
           if(resposta.isEmpty() || binding.promptInput.getText() == null) return;

           UserAnswerValidatorRequestDTO dto = new UserAnswerValidatorRequestDTO(
                contextoTela,
                   binding.titleText.getText().toString(),
                   resposta,
                   tiposPendencia,
                   descricaoDuvida
           );

            answerValidatorController.validarRespostaDaNecessidade(dto);
        });
    }

    //TODO: develop voice input function


}
