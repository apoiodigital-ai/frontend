package com.example.apoiodigital.feature.modal.service;

import com.example.apoiodigital.feature.api.ApiService;
import com.example.apoiodigital.core.Network.RetrofitClient;
import com.example.apoiodigital.feature.modal.data.RequisicaoInput;
import com.example.apoiodigital.feature.modal.data.RequisicaoResponse;

import retrofit2.Call;

public class RequisicaoService {

    private final ApiService apiService =
            RetrofitClient.getSlowRetrofitInstance().create(ApiService.class);

    public Call<RequisicaoResponse> post(RequisicaoInput requisicaoInput) {
        return apiService.enviarRequisicao(requisicaoInput);
    }
}
