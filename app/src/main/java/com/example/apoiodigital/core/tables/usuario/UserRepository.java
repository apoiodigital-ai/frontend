package com.example.apoiodigital.core.tables.usuario;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.apoiodigital.feature.auth.data.TokenResponseDTO;
import com.example.apoiodigital.feature.usuario.data.UserIDDTO;
import com.example.apoiodigital.core.Network.RetrofitClient;
import com.example.apoiodigital.feature.api.ApiService;

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
