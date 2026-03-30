package com.example.apoiodigital.Repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.apoiodigital.Dto.TokenResponseDTO;
import com.example.apoiodigital.Dto.UserIDDTO;
import com.example.apoiodigital.Model.User;
import com.example.apoiodigital.Network.RetrofitClient;
import com.example.apoiodigital.Service.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class UserRepository {
    private ApiService apiService, fastApiService;
    private static UserRepository repository;
    private UserRepository() {
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        fastApiService = RetrofitClient.getFastRetrofitInstance().create(ApiService.class);
    }

    public static UserRepository getRepository() {
        if(repository == null) repository = new UserRepository();

        return repository;
    }

    public MutableLiveData<Boolean> createUser(User user) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();

        apiService.criarConta(user).enqueue(new retrofit2.Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                result.setValue(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                result.setValue(false);
                Log.e("UserRepository", "Erro ao criar usuário", t);
            }
        });
        return result;
    }

    public MutableLiveData<TokenResponseDTO> loginuser(String telefone, String senha) {
        MutableLiveData<TokenResponseDTO> result = new MutableLiveData<>();

        fastApiService.autenticarUsuario(telefone, senha).enqueue(new Callback<TokenResponseDTO>() {
            @Override
            public void onResponse(Call<TokenResponseDTO> call, Response<TokenResponseDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                    Log.d("UserRepository", "Login bem-sucedido. Tokens recebidos.");
                } else {
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

    public MutableLiveData<UserIDDTO> getUserIDByToken(String token){
        MutableLiveData<UserIDDTO> result = new MutableLiveData<>();

        fastApiService.pegarUsuarioIdPorToken(token).enqueue(new Callback<UserIDDTO>() {
            @Override
            public void onResponse(Call<UserIDDTO> call, Response<UserIDDTO> response) {
                if(response.isSuccessful() && response.body()!= null){
                    result.setValue(response.body());
                    Log.d("UserRepository", "ID recebido.");
                }
            }

            @Override
            public void onFailure(Call<UserIDDTO> call, Throwable t) {
                result.setValue(null);
                Log.d("UserRepository", "ID não foi recebido.");
            }
        });

        return result;
    }
}
