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
import androidx.lifecycle.ViewModelProvider;

import com.example.apoiodigital.R;
import com.example.apoiodigital.Model.User;
import com.example.apoiodigital.Utils.PhoneUtils;
import com.example.apoiodigital.Utils.ValidationUtils;
import com.example.apoiodigital.ViewModel.UserViewModel;
import com.google.android.material.button.MaterialButton;

public class RegisterActivity extends BaseActivity {

    private UserViewModel viewModel;
    private EditText inputUsername, inputUserTel, inputUserSenha;
    private MaterialButton btnContinue, toLoginPageBtn;
    private TextView txtViewPasswordR, txtViewTelR, txtViewNameR, txtViewTitle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);

        setContentView(R.layout.activity_register);

        inputUsername = findViewById(R.id.inputUsername);
        inputUserTel = findViewById(R.id.inputUserTelR);
        inputUserSenha = findViewById(R.id.inputUserPasswordR);
        btnContinue = findViewById(R.id.btnContinueR);
        ImageButton backBtn = findViewById(R.id.backBtn);
        toLoginPageBtn = findViewById(R.id.btnLoginR);
        txtViewNameR = findViewById(R.id.txtViewNameR);
        txtViewTelR = findViewById(R.id.txtViewTelR);
        txtViewPasswordR = findViewById(R.id.txtViewPasswordR);
        txtViewTitle = findViewById(R.id.txtViewTitleR);

        resetAllErrors();

        PhoneUtils.applyPhoneMask(inputUserTel);

        backBtn.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, WelcomeActivity.class));
        });


        btnContinue.setOnClickListener(v -> {
            txtViewPasswordR.setText("Use ao menos 8 caracteres, incluindo maiúsculas e minúsculas.");

            boolean hasError = false;

            String name = inputUsername.getText().toString();
            String telefone = PhoneUtils.getDigits(inputUserTel.getText().toString());
            String senha = inputUserSenha.getText().toString();

            if (!ValidationUtils.isValidName(name)) {
                setErrorBorder(inputUsername);
                txtViewNameR.setVisibility(View.VISIBLE);
                hasError = true;
            }
            if (!ValidationUtils.isValidPhone(telefone)) {
                setErrorBorder(inputUserTel);
                txtViewTelR.setVisibility(View.VISIBLE);
                hasError = true;
            }
            if (!ValidationUtils.isValidPassword(senha)) {
                if(senha.trim().isEmpty()) {
                    txtViewPasswordR.setText("Preencha corretamente!");
                }
                setErrorBorder(inputUserSenha);
                txtViewPasswordR.setVisibility(View.VISIBLE);
                hasError = true;
            }

            if (hasError) {
                new Handler(Looper.getMainLooper()).postDelayed(this::resetAllErrors, 5000);
                return;
            }
//                startActivity(new Intent(RegisterActivity.this, MainActivity.class));

                cadastrar(name, telefone, senha);
        });


        toLoginPageBtn.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
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

    private void setErrorTelRegistered(TextView txtViewTitle){
        txtViewTitle.setText("Telefone já cadastrado");
        txtViewTitle.setBackground(getDrawable(R.drawable.text_view_error_bg));
        txtViewTitle.setTextColor(ContextCompat.getColor(this, R.color.white));

    }
    private void resetErrorTelRegistered(TextView txtViewTitle){
        txtViewTitle.setText("Vamos criar uma conta!");
        txtViewTitle.setBackground(null);
        txtViewTitle.setTextColor(ContextCompat.getColor(this, R.color.black));
    }

    private void resetAllErrors() {
        resetBorder(inputUsername);
        resetBorder(inputUserTel);
        resetBorder(inputUserSenha);

        txtViewNameR.setVisibility(View.INVISIBLE);
        txtViewTelR.setVisibility(View.INVISIBLE);
        txtViewPasswordR.setVisibility(View.INVISIBLE);
    }

    private void cadastrar(String name, String telefone, String senha){
        viewModel.registerUser(new User(name, telefone, senha))
                .observe(this, created -> {
                    if (created) {
                        Toast.makeText(this, "Usuário criado com sucesso!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        setErrorTelRegistered(txtViewTitle);
                        Toast.makeText(this, "Erro ao criar usuário", Toast.LENGTH_SHORT).show();
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            resetErrorTelRegistered(txtViewTitle);
                        }, 10000);
                    }
                });
    }

}
