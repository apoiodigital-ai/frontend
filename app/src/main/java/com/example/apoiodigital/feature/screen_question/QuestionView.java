package com.example.apoiodigital.feature.screen_question;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.apoiodigital.databinding.QuestionLayoutBinding;

import java.util.List;

public class QuestionView extends FrameLayout {

    private final CarrosselService carrosselService;
    private final InputService inputService;

    private final QuestionLayoutBinding questionLayoutBinding;

    public QuestionView(@NonNull Context context) {
        super(context);

        // Ensure QuestionView itself fills the parent (which is a ConstraintLayout in OverlayService)
        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT);
        lp.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        lp.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        lp.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        lp.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        this.setLayoutParams(lp);

        questionLayoutBinding = QuestionLayoutBinding.inflate(LayoutInflater.from(context), this, true);

        carrosselService = new CarrosselService(questionLayoutBinding, context);
        inputService = new InputService(questionLayoutBinding);

    }

    public void setInput(InputService.InputListener inputListener){
        inputService.setClickPromptButton(inputListener);
    }

    public void setCarrossel(List<String> opcoesBackend){
        carrosselService.initSetup(opcoesBackend);
    }

    public void setButtonClick(CarrosselService.ButtonListener buttonListener){
        carrosselService.setButtons(buttonListener);
    }

    public void setQuestion(String pergunta){
        questionLayoutBinding.titleText.setText(pergunta);
    }

    public QuestionLayoutBinding getBinding(){
        return questionLayoutBinding;
    }



}
