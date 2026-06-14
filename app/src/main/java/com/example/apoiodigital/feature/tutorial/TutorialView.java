package com.example.apoiodigital.feature.tutorial;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.apoiodigital.databinding.OverlayLayoutBinding;
import com.example.apoiodigital.databinding.TuturialLayoutBinding;
import com.example.apoiodigital.feature.tutorial.data.Bounds;
import com.google.gson.Gson;

public class TutorialView extends FrameLayout { //TODO: fix elements position calculation

    private ConstraintSet respostaConstraint = new ConstraintSet();

    private MaskView mask;
    private final Context context;
    private final WindowManager windowManager;

    private TuturialLayoutBinding binding;
    private final OverlayLayoutBinding overlayLayoutBinding;

    private int height;
    private int width;


    private final BroadcastReceiver receiverBounds = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Gson gson = new Gson();
            String response = intent.getStringExtra("chosedComponentBounds");
            String messageIA = intent.getStringExtra("messageIA");
            if(response == null) return;

            var bounds = gson.fromJson(response, Bounds.class);

            mask.setPositions(bounds);

            binding.mensagem.setText(messageIA);

            var respostaContainer = binding.respostaContainer;

            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) respostaContainer.getLayoutParams();

            respostaContainer.post(() -> {

                if(bounds.getBottom() > height/2){ // esta na parte de baixo

//                    respostaConstraint.connect(respostaContainer.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
                    layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
                    layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET;
                    layoutParams.topMargin = 170;

                }else{

//                    respostaConstraint.connect(respostaContainer.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
                    layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
                    layoutParams.topToTop = ConstraintLayout.LayoutParams.UNSET;

                }
                respostaContainer.setLayoutParams(layoutParams);
//                respostaConstraint.applyTo(respostaContainer);

            });

            setArrowPosition(bounds);

            binding.loadingContainer.setVisibility(View.INVISIBLE);
            binding.tutorialContainer.setVisibility(View.VISIBLE);

            overlayLayoutBinding.closeBtn.setVisibility(View.VISIBLE);
            overlayLayoutBinding.closeBtn.bringToFront();

        }
    };


    public TutorialView(Context context, OverlayLayoutBinding overlayLayoutBinding) {
        super(context);

        this.context = context;
        this.windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        this.overlayLayoutBinding = overlayLayoutBinding;

        // Ensure TutorialView itself fills the parent (which is a ConstraintLayout in OverlayService)
        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT);
        lp.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        lp.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        lp.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        lp.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        this.setLayoutParams(lp);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.apoiodigital.SET_MASK_VIEW");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.registerReceiver(receiverBounds, intentFilter, context.RECEIVER_EXPORTED);
        }

        init();


    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        context.unregisterReceiver(receiverBounds);
    }

    private void init(){

        binding = TuturialLayoutBinding.inflate(LayoutInflater.from(context), this, true);

        mask = binding.mask;
        mask.setContext(context);
        mask.setBinding(binding);
        respostaConstraint.clone(binding.respostaContainer);
    }

    private void setArrowPosition(Bounds bounds){
        binding.setaImg.post(() -> {

            var deltaX = bounds.getRight() - bounds.getLeft();
            var deltaY = bounds.getBottom() - bounds.getTop();

//            Log.e("CryptServiceLOGE", "setArrowPosition > deltaX " + deltaX );


            if( deltaX <= (width/3) ) {

                if( bounds.getLeft() < (width/2) ){ // apontando para esquerda


                    binding.setaImg.setX(bounds.getRight());
                    binding.setaImg.setRotationY(180f);

                }else{ // apontando pra direita



//                    binding.setaImg.getLayoutParams().
                    binding.setaImg.setX(bounds.getLeft()-((float) (3 * deltaX) /2));
//                    binding.setaImg.setRotationX(180f);
                    binding.setaImg.setRotationY(0f);


                }

            }

            if( deltaY <= (height/3) ) {

                if( bounds.getTop() < (height/2) ) { // seta apontando para cima


                    binding.setaImg.setY(bounds.getBottom());
                    binding.setaImg.setRotation(0f);


                }else{ // seta apontando para baixo

                    binding.setaImg.setY((float) (bounds.getTop())-deltaY);
                    binding.setaImg.setRotation(100f);

                }

            }

        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        height = h;
        width = w;
    }
}
