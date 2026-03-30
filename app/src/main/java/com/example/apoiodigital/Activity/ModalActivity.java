package com.example.apoiodigital.Activity;

import android.content.Context;
import android.graphics.Rect;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.apoiodigital.Model.Atalho;
import com.example.apoiodigital.Model.RequisicaoInput;
import com.example.apoiodigital.R;
import com.example.apoiodigital.Recorder.AudioRecorderInput;
import com.example.apoiodigital.ViewModel.modal.ModalViewModel;
import com.example.apoiodigital.databinding.ModalLayoutBinding;

import java.io.File;
import java.util.List;

public class ModalActivity {

    private Boolean isPlaying = false;
    private final AudioRecorderInput recorder;
    private final Context context;


    public ModalActivity(Context _context) {
        context = _context;
        recorder = new AudioRecorderInput(_context);

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

    public void closeModal(ModalLayoutBinding binding, WindowManager windowManager, Context context){
        Animation outAnimation = AnimationUtils.loadAnimation(context, R.anim.out_modal_animation);

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

    public void modalApperingAnimation(ConstraintLayout modal, ImageButton micButton, Context context){
        Animation inAnimation = AnimationUtils.loadAnimation(context, R.anim.in_modal_animation);
        modal.startAnimation(inAnimation);
        micButton.startAnimation(inAnimation);
    }


    public void setSugestoesRapidasBtn(List<Button> btns, List<Atalho> atalhos, ModalViewModel viewModel){
        for(int i = 0; i < btns.size(); i++){
            int finalI = i;
            btns.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    viewModel.iniciarAtalho(atalhos.get(finalI).getId());

                }
            });
        }
    }

    public void setSendInputBtn(ImageButton btn, EditText input, String usuarioID, ModalViewModel viewModel){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                var prompt = input.getText().toString();
                if(usuarioID == null) return;
                if(prompt.equals("") || prompt == null) return;

                var reqInput = new RequisicaoInput(prompt, usuarioID);
                viewModel.enviarRequisicao(reqInput);
                input.setText("");
            }
        });
    }

    public void setVoiceInputBtn(ImageButton micBtn, ModalViewModel viewModel){
        micBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                var filepath = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/voice_input.mp4";

                if(isPlaying){

                    recorder.stop();

                    File audio = new File(filepath);
                    Log.e("STTResponse", "onClick: " + audio.exists() );

                    viewModel.transformarAudioParaTexto(audio);


                    isPlaying = false;

                    micBtn.setImageResource(R.drawable.micbtn);

                    return;

                }

                recorder.start(filepath);
                isPlaying = true;
                micBtn.setImageResource(R.drawable.recording_audio);
            }
        });
    }

}
