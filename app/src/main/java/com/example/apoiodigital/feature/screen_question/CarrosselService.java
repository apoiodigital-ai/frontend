package com.example.apoiodigital.feature.screen_question;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.apoiodigital.R;
import com.example.apoiodigital.databinding.QuestionLayoutBinding;

import java.util.List;

public class CarrosselService {

    private QuestionLayoutBinding questionLayoutBinding;

    private int totalPaginas;
    private Context context;

    private int paginaAtual = 0;

    public CarrosselService(LayoutInflater inflater, ViewGroup root){
        init(inflater, root);

    }

    public void initSetup(List<String> opcoesBackend){

        this.totalPaginas = (int) Math.ceil(opcoesBackend.size() / 2.0);

        setOptions(opcoesBackend);
        generateDots();
        setArrows(opcoesBackend);
    }

    private void init(LayoutInflater inflater, ViewGroup root){

        questionLayoutBinding = QuestionLayoutBinding.inflate(inflater, root, true);

    }

    // This function generate the dots according to the number of pages.
    private void generateDots(){
        questionLayoutBinding.optionsCounterContainer.removeAllViews();

        for(int i = 0; i < totalPaginas; i++){
            ImageView dot = new ImageView(context);

            dot.setImageResource(R.drawable.dot_inactive);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 0, 8, 0);
            dot.setLayoutParams(params);

            questionLayoutBinding.optionsCounterContainer.addView(dot);
        }
    }

    // This function updates the dots according to the page.
    // It receives 'isBack' boolean. If it's true, so the back button was clicked.
    private void updateDots(boolean isBack) {

        ImageView dot = (ImageView) questionLayoutBinding.optionsCounterContainer.getChildAt(paginaAtual);
        dot.setImageResource(R.drawable.dot_active);

        if(isBack) {
            ImageView dotPast = (ImageView) questionLayoutBinding.optionsCounterContainer.getChildAt(paginaAtual+1);
            dotPast.setImageResource(R.drawable.dot_inactive);
            return;
        }
        ImageView dotPast = (ImageView) questionLayoutBinding.optionsCounterContainer.getChildAt(paginaAtual-1);
        dotPast.setImageResource(R.drawable.dot_inactive);

    }

    // This function sets the options according the var 'paginaAtual'.
    private void setOptions(List<String> opcoesBackend){
        if(opcoesBackend.get(paginaAtual+1) != null){
            questionLayoutBinding.firstButton.setText(opcoesBackend.get(paginaAtual));
            questionLayoutBinding.secondButton.setText(opcoesBackend.get(paginaAtual+1));
            questionLayoutBinding.secondButton.setVisibility(VISIBLE);
        }else{
            questionLayoutBinding.firstButton.setText(opcoesBackend.get(paginaAtual));
            questionLayoutBinding.secondButton.setVisibility(INVISIBLE);
        }
    }

    // This function sets the listeners for the buttons.
    private void setArrows(List<String> opcoesBackend){
        questionLayoutBinding.backButton.setOnClickListener(view -> {
            if(paginaAtual > 0){
                paginaAtual--;
                updateDots(true);
                setOptions(opcoesBackend);
            }
            });
        questionLayoutBinding.nextButton.setOnClickListener(view -> {
            if(paginaAtual < totalPaginas-1){
                paginaAtual++;
                updateDots(false);
                setOptions(opcoesBackend);
            }
        });
    }

}
