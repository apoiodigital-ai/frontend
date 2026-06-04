package com.example.apoiodigital.feature.modal;

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

    private final List<Atalho> atalhosCache = new ArrayList<>();
    private Context context;
    private ModalLayoutBinding binding;

    private Boolean isPlaying = false;
    private final AudioRecorderInput recorder;
    private final AudioController audioController;
    private final AtalhoController atalhoController;
    private final RequisicaoController requisicaoController;

    public ModalView(Context context, LayoutInflater inflater, ViewGroup root, AudioController audioController, AtalhoController atalhoController, RequisicaoController requisicaoController) {
        super(context);

        this.context = context;

        recorder = new AudioRecorderInput(context);
        this.audioController = audioController;
        this.atalhoController = atalhoController;
        this.requisicaoController = requisicaoController;

        init(inflater, root);
    }

    private void init(LayoutInflater inflater, ViewGroup root) {

        binding = ModalLayoutBinding.inflate(inflater, this, true);

    }

    public ModalLayoutBinding getBinding() {
        return binding;
    }

    public void setModalSettings(String _userID, List<Atalho> atalhosCache) {

        var windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        closeModal(binding, windowManager, context);
        keyboardValidationActions(binding);

        List<Button> sugestoesRapidasBtn = Arrays.asList(
                binding.sugestoesRapidasBtn,
                binding.sugestoesRapidasBtn2,
                binding.sugestoesRapidasBtn3
        );

        setSugestoesRapidasBtn(sugestoesRapidasBtn, atalhosCache, atalhoController);

        setSendInputBtn(binding.sendPromptBtn, binding.promptInput, _userID, requisicaoController);
        setVoiceInputBtn(binding.voicePromptInput, audioController);
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


    public void setSugestoesRapidasBtn(List<Button> btns, List<Atalho> atalhos, AtalhoController atalhoController){
        for(int i = 0; i < btns.size(); i++){
            int finalI = i;
            btns.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    atalhoController.iniciarAtalho(atalhos.get(finalI).getId());

                }
            });
        }
    }

    public void setSendInputBtn(ImageButton btn, EditText input, String usuarioID, RequisicaoController viewModel){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                var prompt = input.getText().toString();
                Log.e("VIEWMODEL", "onClick: " + prompt );
                if(usuarioID == null) return;
                if(prompt.isEmpty() || input.getText() == null) return;

                var reqInput = new RequisicaoInput(prompt, usuarioID);
                viewModel.enviarRequisicao(reqInput);
                input.setText("");
            }
        });
    }

    public void setVoiceInputBtn(ImageButton micBtn, AudioController viewModel){
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
