package com.example.apoiodigital.feature.login;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.apoiodigital.core.Network.RetrofitClient;
import com.example.apoiodigital.core.Utils.SessionManager;
import com.example.apoiodigital.feature.auth.data.TokenResponseDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {

    private final LoginApiService fastApiService;

    public LoginRepository() {
        fastApiService = RetrofitClient.getFastRetrofitInstance().create(LoginApiService.class);
    }


    public MutableLiveData<TokenResponseDTO> loginuser(String telefone, String senha, LoginCallBack callback) {
        MutableLiveData<TokenResponseDTO> result = new MutableLiveData<>();

        fastApiService.autenticarUsuario(telefone, senha).enqueue(new Callback<TokenResponseDTO>() {
            @Override
            public void onResponse(Call<TokenResponseDTO> call, Response<TokenResponseDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    //TODO: Salvar tokens no SharedPreferences
                    callback.onSuccess();
                    result.setValue(response.body());
                    Log.d("UserRepository", "Login bem-sucedido. Tokens recebidos.");
                } else {
                    callback.onError("Erro ao autenticar usuário");
                    result.setValue(null); // falha → null
                    Log.e("UserRepository", "Erro no login: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<TokenResponseDTO> call, Throwable t) {
                result.setValue(null);
                Log.e("UserRepository", "Erro ao autenticar usuário", t);
            }
        });

        return result;

    }

    public interface LoginCallBack{
        void onSuccess();
        void onError(String error);
    }

}
