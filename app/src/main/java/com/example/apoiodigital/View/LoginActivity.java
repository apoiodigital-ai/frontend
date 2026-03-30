package com.example.apoiodigital.View;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.apoiodigital.R;
import com.example.apoiodigital.Utils.PhoneUtils;
import com.example.apoiodigital.Utils.SessionManager;
import com.example.apoiodigital.Utils.ValidationUtils;
import com.example.apoiodigital.ViewModel.UserViewModel;
import com.google.android.material.button.MaterialButton;

public class LoginActivity extends BaseActivity {

    private UserViewModel viewModel;
    private EditText inputUserTel, inputUserSenha;

    private TextView txtViewTelL, txtViewPasswordL, txtViewTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        MaterialButton btnContinue = findViewById(R.id.btnContinueL);
        MaterialButton btnRegister = findViewById(R.id.btnRegister);
        ImageButton backBtn = findViewById(R.id.backBtnL);

        inputUserTel = findViewById(R.id.inputUserTelL);
        inputUserSenha = findViewById(R.id.inputUserPasswordL);

        txtViewTitle = findViewById(R.id.txtViewTitleL);
        txtViewTelL = findViewById(R.id.txtViewTelL);
        txtViewPasswordL = findViewById(R.id.txtViewPasswordL);

        resetAllErrors();

        PhoneUtils.applyPhoneMask(inputUserTel);

        backBtn.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        btnContinue.setOnClickListener(v -> {
            txtViewPasswordL.setText("Use ao menos 8 caracteres, incluindo maiúsculas e minúsculas.");
//            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//            finish();

            boolean hasError = false;

            String telefone = PhoneUtils.getDigits(inputUserTel.getText().toString());
            String senha = inputUserSenha.getText().toString().trim();

            if (!ValidationUtils.isValidPhone(telefone)) {
                setErrorBorder(inputUserTel);
                txtViewTelL.setVisibility(View.VISIBLE);
                hasError = true;
            }
            if (!ValidationUtils.isValidPassword(senha)) {
                if(senha.trim().isEmpty()) {
                    txtViewPasswordL.setText("Preencha corretamente!");
                }
                setErrorBorder(inputUserSenha);
                txtViewPasswordL.setVisibility(View.VISIBLE);
                hasError = true;
            }

            if (hasError) {
                new Handler(Looper.getMainLooper()).postDelayed(this::resetAllErrors, 5000);
                return;
            }
//            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            realizarLogin(telefone, senha);
        });

        btnRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void realizarLogin(String telefone, String senha){
        viewModel = new UserViewModel();

        viewModel.autenticar(telefone, senha).observe(this, tokenResponse -> {

            if (tokenResponse != null) {
                Toast.makeText(this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();

                String accessToken = tokenResponse.getAccessToken();
                String refreshToken = tokenResponse.getRefreshToken();

                SessionManager sessionManager = new SessionManager(this);
                sessionManager.saveTokens(accessToken, refreshToken);

                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Erro ao entrar na conta!", Toast.LENGTH_SHORT).show();
                setErrorTelIncorrect(txtViewTitle);
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    resetErrorTelIncorrect(txtViewTitle);
                }, 10000);
            }
        });
    }
    private void setErrorBorder(EditText editText) {
        GradientDrawable bg = (GradientDrawable) editText.getBackground();
        bg.setStroke(4, ContextCompat.getColor(this, R.color.RedAD));
    }

    private void resetBorder(EditText editText) {
        GradientDrawable bg = (GradientDrawable) editText.getBackground();
        bg.setStroke(4, ContextCompat.getColor(this, R.color.DarkGrayAD));
    }

    private void setErrorTelIncorrect(TextView txtViewTitle){
        txtViewTitle.setText("Login incorreto");
        txtViewTitle.setBackground(getDrawable(R.drawable.text_view_error_bg));
        txtViewTitle.setTextColor(ContextCompat.getColor(this, R.color.white));

    }
    private void resetErrorTelIncorrect(TextView txtViewTitle){
        txtViewTitle.setText("Entre na sua conta!");
        txtViewTitle.setBackground(null);
        txtViewTitle.setTextColor(ContextCompat.getColor(this, R.color.black));
    }

    private void resetAllErrors() {
        resetBorder(inputUserTel);
        resetBorder(inputUserSenha);

        txtViewTelL.setVisibility(View.INVISIBLE);
        txtViewPasswordL.setVisibility(View.INVISIBLE);
    }
}
