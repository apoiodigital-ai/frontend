package com.example.apoiodigital.feature.modal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.apoiodigital.feature.lauch.BaseActivity;
import com.example.apoiodigital.feature.overlay.OverlayService;

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
