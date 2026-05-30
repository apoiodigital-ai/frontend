package com.example.apoiodigital.Service;

import com.example.apoiodigital.Dto.FindBestAnswerResponseDTO;
import com.example.apoiodigital.Model.FindBestAnswerRequestDTO;
import com.example.apoiodigital.Network.RetrofitClient;

import retrofit2.Call;

public class RespostaService {

    private final ApiService apiService =
            RetrofitClient.getSlowRetrofitInstance().create(ApiService.class);

    public Call<FindBestAnswerResponseDTO> getMessageIA(FindBestAnswerRequestDTO requestDTO) {
        return apiService.exigirRespostaIA(requestDTO);
    }
}
