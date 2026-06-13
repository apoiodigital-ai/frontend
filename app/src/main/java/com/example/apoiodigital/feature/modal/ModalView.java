package com.example.apoiodigital.feature.modal;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.graphics.Rect;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.apoiodigital.R;
import com.example.apoiodigital.core.Utils.SessionManager;
import com.example.apoiodigital.core.tables.usuario.UsuarioController;
import com.example.apoiodigital.databinding.ModalLayoutBinding;
import com.example.apoiodigital.core.tables.atalho.Atalho;
import com.example.apoiodigital.feature.Recorder.AudioRecorderInput;
import com.example.apoiodigital.feature.modal.data.RequisicaoInput;
import com.example.apoiodigital.feature.modal.viewmodel.AtalhoController;
import com.example.apoiodigital.feature.modal.viewmodel.AudioController;
import com.example.apoiodigital.feature.modal.viewmodel.RequisicaoController;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModalView extends FrameLayout {

    private final Context context;
    private ModalLayoutBinding binding;

    private ModalListener modalListener;

    public ModalView(Context context) {
        super(context);

        this.context = context;

        init();
    }

    private void init() {

        binding = ModalLayoutBinding.inflate(LayoutInflater.from(context), this, true);

    }

    public ModalLayoutBinding getBinding() {
        return binding;
    }

    public void setModalSettings() {

        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);

        closeModal(binding, windowManager);
        keyboardValidationActions(binding);

        setSugestoesRapidasBtn();

        setSendInputBtn(binding.sendPromptBtn, binding.promptInput);

    }

    public void setListener(ModalListener modalListener){
        this.modalListener = modalListener;
    }

    public void keyboardValidationActions(ModalLayoutBinding binding){
        View rootView = binding.getRoot().getRootView();
        ConstraintLayout sugestoesRapidasLayout = binding.sugestoesRapidasLayout;

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect auxRect = new Rect();
            rootView.getWindowVisibleDisplayFrame(auxRect);

            int screentHeight = rootView.getRootView().getHeight();
            int actHeight = screentHeight - auxRect.bottom;


            if(actHeight > screentHeight * 0.15){ // 15% 'e para margem de erro

                rootView.setTranslationY((float) -actHeight /2);
                sugestoesRapidasLayout.setVisibility(View.GONE);



            }else{

                rootView.setTranslationY((float) actHeight /2);
                sugestoesRapidasLayout.setVisibility(View.VISIBLE);

            }
        });
    }

    public void closeModal(ModalLayoutBinding binding, WindowManager windowManager){
        Animation outAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.out_modal_animation);

        View viewToCloseModal = binding.viewToCloseModal;
        viewToCloseModal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                outAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {

                        windowManager.removeView(binding.getRoot().getRootView());
//                        finishAffinity(activity);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }

                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                });

                binding.constraintLayout.startAnimation(outAnimation);
                binding.voicePromptInput.startAnimation(outAnimation);

            }
        });
    }

    public void setSugestoesRapidasBtn(){ //TODO: fix init Atalho feature
        for(int i = 0; i < 3; i++){
            int finalI = i;
            binding.sugestoesRapidasSubLayout.getChildAt(i+1).setOnClickListener(new View.OnClickListener() { // because the first child is a text
                @Override
                public void onClick(View view) {

                    modalListener.atalhoInit(finalI);

                }
            });
        }
    }

    public void setSendInputBtn(ImageButton btn, EditText input){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                var prompt = input.getText().toString();
                Log.e("VIEWMODEL", "onClick: " + prompt );
//                if(usuarioID == null) return;
//                if(prompt.isEmpty() || input.getText() == null) return;
//
//                var reqInput = new RequisicaoInput(prompt, usuarioID);
//                viewModel.enviarRequisicao(reqInput);

                modalListener.onPromptSent(prompt);
                input.setText("");
            }
        });
    }

    public interface ModalListener{
        void onPromptSent(String prompt);
        void atalhoInit(int index);

    }

}
