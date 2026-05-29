package com.example.apoiodigital.Repository;

import com.example.apoiodigital.Model.CryptedRequestIA;
import com.example.apoiodigital.Service.RespostaService;

import retrofit2.Call;

public class TutorialRepository {

    private final RespostaService respostaService;

    public TutorialRepository(RespostaService respostaService) {
        this.respostaService = respostaService;
    }

    public Call<String> getIaMessage(CryptedRequestIA requestDTO) {
        return respostaService.getMessageIA(requestDTO);
    }
}
