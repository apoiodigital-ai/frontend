package com.example.apoiodigital.feature.screen_question;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import java.util.List;

public class QuestionView extends FrameLayout {

    private CarrosselService carrosselService;
    private InputService inputService;

    public QuestionView(@NonNull Context context) {
        super(context);

        carrosselService = new CarrosselService(LayoutInflater.from(this.getContext()), this);
        inputService = new InputService(LayoutInflater.from(this.getContext()), this);

    }

    public void setInput(InputService.InputListener inputListener){
        inputService.setClickPromptButton(inputListener);
    }

    public void setCarrossel(List<String> opcoesBackend){
        carrosselService.initSetup(opcoesBackend);
    }



}
