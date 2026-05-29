package com.example.apoiodigital.Service;

import com.example.apoiodigital.Model.Atalho;
import com.example.apoiodigital.Network.RetrofitClient;

import java.util.List;

import retrofit2.Call;

public class AtalhoService {

    private final ApiService apiService =
            RetrofitClient.getRetrofitInstance().create(ApiService.class);

    public Call<List<Atalho>> getAll(String usuarioID) {
        return apiService.carregarAtalhos(usuarioID);
    }

    public Call<Void> initByID(String atalhoID) {
        return apiService.iniciarAtalho(atalhoID);
    }
}
