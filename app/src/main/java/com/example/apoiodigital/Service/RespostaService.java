package com.example.apoiodigital.Service;

import com.example.apoiodigital.Model.FindBestAnswerRequestDTO;
import com.example.apoiodigital.Network.RetrofitClient;

import retrofit2.Call;

public class RespostaService {

    private final ApiService apiService =
            RetrofitClient.getSlowRetrofitInstance().create(ApiService.class);

    public Call<String> getMessageIA(FindBestAnswerRequestDTO requestDTO) {
        return apiService.exigirRespostaIA(requestDTO);
    }
}
