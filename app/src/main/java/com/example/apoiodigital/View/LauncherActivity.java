package com.example.apoiodigital.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.apoiodigital.R;
import com.example.apoiodigital.Dto.AccessTokenDTO;
import com.example.apoiodigital.Network.RetrofitClient;
import com.example.apoiodigital.Repository.AuthRepository;
import com.example.apoiodigital.Service.ApiService;
import com.example.apoiodigital.Utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LauncherActivity extends BaseActivity {
    private SessionManager sessionManager;
    private AuthRepository authRepository;
    private ProgressBar progressBar;
    private TextView textViewError;


    @Override   
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        sessionManager = new SessionManager(this);
        authRepository = new AuthRepository(sessionManager, RetrofitClient.getFastRetrofitInstance().create(ApiService.class));


        textViewError = findViewById(R.id.errorMsgTextViewA);
        progressBar = findViewById(R.id.progressBarLauncher);

//        Checagem de internet e api
//        if(NetworkUtil.isConnectedToInternet(this)){
//            checkTokensAndNavigate();
//        }
//        else{
//            new Handler(Looper.getMainLooper()).postDelayed(() -> {
//                textViewError.setText("Por favor, conecte-se à internet para usar o aplicativo");
//                progressBar.setVisibility(View.GONE);
//                textViewError.setVisibility(View.VISIBLE);
//            }, 7000);
//
//            redirectToWelcome();
//        }

        String refreshToken = sessionManager.getRefreshToken();
        String accessToken = sessionManager.getAccessToken();
        if (accessToken != null && refreshToken != null) {
            startActivity(new Intent(LauncherActivity.this, MainActivity.class));
            finish();
            return;
        }
        startActivity(new Intent(this, WelcomeActivity.class));
        finish();
    }

    private void checkTokensAndNavigate() {
        String refreshToken = sessionManager.getRefreshToken();

        if (refreshToken != null) {
            authRepository.refreshToken(new Callback<AccessTokenDTO>() {
                @Override
                public void onResponse(Call<AccessTokenDTO> call, Response<AccessTokenDTO> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        progressBar.setVisibility(View.GONE);
                        sessionManager.saveTokens(response.body().getAccessToken(), refreshToken);
                        redirectToMain();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        redirectToWelcome();
                    }
                }

                @Override
                public void onFailure(Call<AccessTokenDTO> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    textViewError.setVisibility(View.VISIBLE);
                }
            });
        } else {
            Log.d("Conexão", "Tá nulo");
            redirectToWelcome();
        }
    }


    private void redirectToWelcome(){
        startActivity(new Intent(LauncherActivity.this, WelcomeActivity.class));
        finish();
    }

    private void redirectToMain(){
        startActivity(new Intent(LauncherActivity.this, MainActivity.class));
        finish();
    }
}
