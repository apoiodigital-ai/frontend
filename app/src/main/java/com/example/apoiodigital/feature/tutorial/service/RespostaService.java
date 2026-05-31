package com.example.apoiodigital.feature.tutorial.service;

import com.example.apoiodigital.feature.tutorial.data.FindBestAnswerResponseDTO;
import com.example.apoiodigital.feature.tutorial.data.FindBestAnswerRequestDTO;
import com.example.apoiodigital.feature.api.ApiService;
import com.example.apoiodigital.core.Network.RetrofitClient;

import retrofit2.Call;

public class RespostaService {

    private final ApiService apiService =
            RetrofitClient.getSlowRetrofitInstance().create(ApiService.class);

    public Call<FindBestAnswerResponseDTO> getMessageIA(FindBestAnswerRequestDTO requestDTO) {
        return apiService.exigirRespostaIA(requestDTO);
    }
}
