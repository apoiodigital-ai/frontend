package com.example.apoiodigital.View;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.apoiodigital.R;
import com.google.android.material.button.MaterialButton;

public class WelcomeActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);

        MaterialButton btnContinue = findViewById(R.id.btnContinueW);
        btnContinue.setOnClickListener(v -> {
            startActivity(new Intent(WelcomeActivity.this, RegisterActivity.class));
            finish();
        });

    }

}
