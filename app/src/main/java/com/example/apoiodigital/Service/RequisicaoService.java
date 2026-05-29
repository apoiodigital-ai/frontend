package com.example.apoiodigital.Service;

import com.example.apoiodigital.Model.RequisicaoInput;
import com.example.apoiodigital.Model.RequisicaoResponse;
import com.example.apoiodigital.Network.RetrofitClient;

import retrofit2.Call;

public class RequisicaoService {

    private final ApiService apiService =
            RetrofitClient.getSlowRetrofitInstance().create(ApiService.class);

    public Call<RequisicaoResponse> post(RequisicaoInput requisicaoInput) {
        return apiService.enviarRequisicao(requisicaoInput);
    }
}
