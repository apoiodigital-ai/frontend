package com.example.apoiodigital.feature.screen_question;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.apoiodigital.databinding.QuestionLayoutBinding;
import com.example.apoiodigital.feature.tutorial.data.TiposPendencia;

public class InputService {

    private final QuestionLayoutBinding binding;

    public InputService(QuestionLayoutBinding binding) {

        this.binding = binding;

    }


    public void setClickPromptButton(InputListener inputListener){
        binding.sendPromptBtn.setOnClickListener(view -> {
           String resposta = binding.promptInput.getText().toString();
           if(resposta.isEmpty() || binding.promptInput.getText() == null) {
               return;
           }

            inputListener.onPromptButtonClick(binding.titleText.getText().toString(), resposta);

        });
    }

    //TODO: develop voice input function

    public interface InputListener{
        void onPromptButtonClick(String pergunta, String resposta);
    }
}
