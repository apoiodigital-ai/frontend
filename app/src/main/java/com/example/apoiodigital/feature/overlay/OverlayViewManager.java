package com.example.apoiodigital.feature.overlay;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;

import com.example.apoiodigital.feature.modal.ModalView;
import com.example.apoiodigital.feature.tutorial.TutorialView;

public class OverlayViewManager {

    private final Context context;
    private final WindowManager windowManager;
    private final WindowManagerService windowManagerService;
    private View currentView;

    public OverlayViewManager(Context context) {
        windowManagerService = new WindowManagerService();

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

    public void showModalView(ModalView.ModalListener listener) {
        removeCurrentView();

        ModalView modal = new ModalView(context);
        modal.setListener(listener);

        windowManager.addView(modal, windowManagerService.getWindowParams());
        currentView = modal;
    }

    public void showTutorialView() {
        removeCurrentView();

        TutorialView tutorial = new TutorialView(context);
        windowManager.addView(tutorial, windowManagerService.getWindowParamsForTutorial());
        currentView = tutorial;
    }





}
