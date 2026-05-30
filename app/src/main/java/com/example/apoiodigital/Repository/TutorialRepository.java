package com.example.apoiodigital.Repository;

import com.example.apoiodigital.Dto.FindBestAnswerResponseDTO;
import com.example.apoiodigital.Model.FindBestAnswerRequestDTO;
import com.example.apoiodigital.Service.RespostaService;

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
