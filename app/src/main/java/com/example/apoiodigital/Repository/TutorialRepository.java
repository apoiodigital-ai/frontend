package com.example.apoiodigital.Repository;

import okhttp3.Call;

import com.example.apoiodigital.Model.CryptedRequestIA;
import com.example.apoiodigital.Service.RespostaService;

public class TutorialRepository {

    private final RespostaService respostaService;


    public TutorialRepository(RespostaService respostaService) {
        this.respostaService = respostaService;
    }

    public Call getIaMessage(CryptedRequestIA requestDTO){
        return respostaService.getMessageIA(requestDTO);
    }
}
