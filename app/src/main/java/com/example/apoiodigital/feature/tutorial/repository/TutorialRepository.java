package com.example.apoiodigital.feature.tutorial.repository;

import com.example.apoiodigital.feature.tutorial.data.FindBestAnswerResponseDTO;
import com.example.apoiodigital.feature.tutorial.data.FindBestAnswerRequestDTO;
import com.example.apoiodigital.core.tables.resposta.RespostaService;

import retrofit2.Call;

public class TutorialRepository {

    private final RespostaService respostaService;

    public TutorialRepository(RespostaService respostaService) {
        this.respostaService = respostaService;
    }

    public Call<FindBestAnswerResponseDTO> getIaMessage(FindBestAnswerRequestDTO requestDTO) {
        return respostaService.getMessageIA(requestDTO);
    }
}
