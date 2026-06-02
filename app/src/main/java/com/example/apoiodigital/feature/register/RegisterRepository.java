package com.example.apoiodigital.feature.register;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.apoiodigital.core.Network.RetrofitClient;
import com.example.apoiodigital.core.tables.usuario.Usuario;
import com.example.apoiodigital.feature.api.ApiService;

import retrofit2.Call;
import retrofit2.Response;

public class RegisterRepository {

    private RegisterApiService apiService;

    public RegisterRepository() {
        apiService = RetrofitClient.getRetrofitInstance().create(RegisterApiService.class);
    }

    public MutableLiveData<Boolean> createUser(Usuario usuario, RegisterCallBack callback) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();

        apiService.criarConta(usuario).enqueue(new retrofit2.Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                result.setValue(response.isSuccessful());
                callback.onSuccess();
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                result.setValue(false);
                callback.onError("Erro ao criar usuário");
                Log.e("UserRepository", "Erro ao criar usuário", t);
            }
        });
        return result;
    }

    public interface RegisterCallBack{
        void onSuccess();
        void onError(String error);
    }

}
