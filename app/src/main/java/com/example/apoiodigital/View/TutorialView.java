package com.example.apoiodigital.View;

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

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.apoiodigital.Activity.TutorialActivity;
import com.example.apoiodigital.Model.Bounds;
import com.example.apoiodigital.Model.RequisicaoResponse;
import com.example.apoiodigital.R;
import com.example.apoiodigital.databinding.TuturialLayoutBinding;
import com.google.gson.Gson;

public class TutorialView extends View{

    private ConstraintSet respostaConstraint = new ConstraintSet();

    private final BroadcastReceiver receiverBounds = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Gson gson = new Gson();
            String response = intent.getStringExtra("chosedComponentBounds");
            String messageIA = intent.getStringExtra("messageIA");
            if(response == null) return;

            var bounds = gson.fromJson(response, Bounds.class);
            Log.e("CryptServiceLOGE", "onReceiveBounds: " + (bounds.getBottom() > getHeight()/2));
            Log.e("CryptServiceLOGE", "onReceiveBounds: " + getHeight()/2);
            mask.setPositions(bounds);

            binding.mensagem.setText(messageIA);

            var respostaContainer = binding.respostaContainer;

            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) respostaContainer.getLayoutParams();

            respostaContainer.post(() -> {

                if(bounds.getBottom() > getHeight()/2){ // esta na parte de baixo

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
        }
    };

    private MaskView mask;
    private final Context context;
    private final WindowManager windowManager;

    private TuturialLayoutBinding binding;

    public TutorialView(Context context, LayoutInflater inflater, ViewGroup root, WindowManager windowManager) {
        super(context);

        this.context = context;
        this.windowManager = windowManager;

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.apoiodigital.SET_MASK_VIEW");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.registerReceiver(receiverBounds, intentFilter, context.RECEIVER_EXPORTED);
        }


//        context.sendBroadcast(new Intent("com.example.apoiodigital.SEND_TO_IA"));

        init(inflater, root);

        var activity = new TutorialActivity();

        binding.closeBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("ANIVAPAFPWAFW", "onClick: CLICOU CLICOU" );
                windowManager.removeView(binding.getRoot());

            }
        });

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        context.unregisterReceiver(receiverBounds);
    }

    private void init(LayoutInflater inflater, ViewGroup root){
        var view = inflater.inflate(R.layout.tuturial_layout, root, true);
        binding = TuturialLayoutBinding.bind(view);
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
            Log.e("CryptServiceLOGE", "setArrowPosition > h/2 " + getHeight()/2 );


            if( deltaX <= (getWidth()/3) ) {

                if( bounds.getLeft() < (getWidth()/2) ){ // apontando para esquerda

                    Log.e("CryptServiceLOGE", "setArrowPosition: apontando para esquerda" );
                    binding.setaImg.setX(bounds.getRight());
                    binding.setaImg.setRotationY(180f);

                }else{ // apontando pra direita

                    Log.e("CryptServiceLOGE", "setArrowPosition: apontando para direita" );

//                    binding.setaImg.getLayoutParams().
                    binding.setaImg.setX(bounds.getLeft()-((float) (3 * deltaX) /2));
//                    binding.setaImg.setRotationX(180f);
                    binding.setaImg.setRotationY(0f);


                }

            }

            if( deltaY <= (getHeight()/3) ) {

                if( bounds.getTop() < (getHeight()/2) ) { // seta apontando para cima

//                    bottom = bounds.getTop();
                    Log.e("CryptServiceLOGE", "setArrowPosition: apontando para cima" );

                    binding.setaImg.setY(bounds.getBottom());
                    binding.setaImg.setRotation(0f);


                }else{ // seta apontando para baixo

                    Log.e("CryptServiceLOGE", "setArrowPosition: apontando para baixo" );

                    binding.setaImg.setY((float) (bounds.getTop())-deltaY);
                    binding.setaImg.setRotation(100f);

                }

            }

        });
    }




}
