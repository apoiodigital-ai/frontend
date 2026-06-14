package com.example.apoiodigital.feature.overlay;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;

import com.example.apoiodigital.databinding.OverlayLayoutBinding;
import com.example.apoiodigital.feature.modal.ModalView;
import com.example.apoiodigital.feature.screen_question.QuestionView;
import com.example.apoiodigital.feature.tutorial.TutorialView;

public class OverlayViewManager {

    private final Context context;
    private final WindowManager windowManager;
    private final WindowManagerUtils windowManagerUtils;
    private View currentView;

    public OverlayViewManager(Context context) {
        windowManagerUtils = new WindowManagerUtils();

        this.context = context;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    public View getCurrentView() {
        return currentView;
    }

    public void removeCurrentView() {
        if (currentView != null && currentView.isAttachedToWindow()) {
            windowManager.removeView(currentView);
            currentView = null;
        }
    }

    public void showModalView(ModalView.ModalListener listener, OverlayLayoutBinding overlayLayoutBinding) {
        removeCurrentView();

        ModalView modal = new ModalView(context);
        modal.setListener(listener);

        overlayLayoutBinding.closeBtn.setVisibility(View.INVISIBLE);

        windowManager.addView(modal, windowManagerUtils.getWindowParams());
        currentView = modal;
    }

    public void showTutorialView(View mainOverlay, OverlayLayoutBinding overlayLayoutBinding) {
        removeCurrentView();

        TutorialView tutorial = new TutorialView(context, overlayLayoutBinding);
        windowManager.updateViewLayout(mainOverlay, windowManagerUtils.getWindowParamsForTutorial());

        overlayLayoutBinding.container.removeAllViews();
        overlayLayoutBinding.container.addView(tutorial);

        overlayLayoutBinding.closeBtn.setVisibility(View.VISIBLE);
        overlayLayoutBinding.closeBtn.bringToFront();

        currentView = tutorial;

        //TODO: needs to fix the close button when mask is appearing
        overlayLayoutBinding.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Fix: Remove the top-level overlay view from WindowManager

                windowManager.removeView(mainOverlay);

            }
        });
    }

    public void showQuestionView(View mainOverlay, OverlayLayoutBinding overlayLayoutBinding){

        removeCurrentView();

        QuestionView questionView = new QuestionView(context);
        windowManager.updateViewLayout(mainOverlay, windowManagerUtils.getWindowParams());

        overlayLayoutBinding.container.removeAllViews();
        overlayLayoutBinding.container.addView(questionView);

        overlayLayoutBinding.closeBtn.bringToFront();

        currentView = questionView;

    }





}
