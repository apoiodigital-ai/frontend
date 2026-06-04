package com.example.apoiodigital.feature.modal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.apoiodigital.R;
import com.example.apoiodigital.core.tables.atalho.Atalho;
import com.example.apoiodigital.core.tables.usuario.UsuarioController;
import com.example.apoiodigital.databinding.ModalLayoutBinding;
import com.example.apoiodigital.databinding.OverlayLayoutBinding;
import com.example.apoiodigital.feature.lauch.BaseActivity;
import com.example.apoiodigital.feature.modal.data.RequisicaoInput;
import com.example.apoiodigital.feature.modal.service.OverlayService;
import com.example.apoiodigital.feature.modal.service.TokenService;
import com.example.apoiodigital.feature.modal.service.WindowManagerService;
import com.example.apoiodigital.feature.tutorial.TutorialView;

import java.util.ArrayList;
import java.util.List;

public class AskHelpActivity extends BaseActivity {

    private static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 5469;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);

            intent.addCategory(Intent.CATEGORY_HOME);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);

            iniciarServicoAssistente();
        }

    }

    private void iniciarServicoAssistente() {
        Intent intent = new Intent(this, OverlayService.class);
        ContextCompat.startForegroundService(this, intent);
        finish(); // A Activity morre imediatamente, liberando a memória
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (Settings.canDrawOverlays(this)) {
                iniciarServicoAssistente();
            }
        }
    }
}
