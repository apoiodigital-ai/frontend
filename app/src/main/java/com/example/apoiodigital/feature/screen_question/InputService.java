package com.example.apoiodigital.feature.screen_question;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.apoiodigital.databinding.QuestionLayoutBinding;
import com.example.apoiodigital.feature.tutorial.data.TiposPendencia;

public class InputService {

    private QuestionLayoutBinding binding;

    public InputService(LayoutInflater inflater, ViewGroup root) {

        init(inflater, root);

    }

    private void init(LayoutInflater inflater, ViewGroup root){

        binding = QuestionLayoutBinding.inflate(inflater, root, true);

    }

    public void setClickPromptButton(InputListener inputListener){
        binding.sendPromptBtn.setOnClickListener(view -> {
           String resposta = binding.promptInput.getText().toString();
           if(resposta.isEmpty() || binding.promptInput.getText() == null) return;

            inputListener.onPromptButtonClick(binding.titleText.getText().toString(), resposta);

//           UserAnswerValidatorRequestDTO dto = new UserAnswerValidatorRequestDTO(
//                contextoTela,
//                   binding.titleText.getText().toString(),
//                   resposta,
//                   tiposPendencia,
//                   descricaoDuvida
//           );
//
//            answerValidatorController.validarRespostaDaNecessidade(dto);
        });
    }

    //TODO: develop voice input function

    public interface InputListener{
        void onPromptButtonClick(String pergunta, String resposta);
    }
}
