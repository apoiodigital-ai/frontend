package com.example.apoiodigital.core.tables.resposta;

import com.example.apoiodigital.feature.tutorial.data.ChecksInformationNeedsRequestDTO;
import com.example.apoiodigital.feature.tutorial.data.ChecksInformationNeedsResponseDTO;
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

    public Call<ChecksInformationNeedsResponseDTO> validarNecessidadeInformacoes(ChecksInformationNeedsRequestDTO requestDTO){
        return apiService.validarNecessidadeInformacoes(requestDTO);
    }

}
