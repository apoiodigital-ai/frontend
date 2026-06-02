package com.example.apoiodigital.core.tables.atalho;

import com.example.apoiodigital.feature.api.ApiService;
import com.example.apoiodigital.core.Network.RetrofitClient;
import com.example.apoiodigital.core.tables.requisicao.Requisicao;

import java.util.List;

import retrofit2.Call;

public class AtalhoService {

    private final ApiService apiService =
            RetrofitClient.getRetrofitInstance().create(ApiService.class);

    public Call<List<Atalho>> getAll(String usuarioID) {
        return apiService.carregarAtalhos(usuarioID);
    }

    public Call<Requisicao> initByID(String atalhoID) {
        return apiService.iniciarAtalho(atalhoID);
    }
}
