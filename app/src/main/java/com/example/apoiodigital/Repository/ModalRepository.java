package com.example.apoiodigital.Repository;

import com.example.apoiodigital.Model.RequisicaoInput;
import com.example.apoiodigital.Service.AtalhoService;
import com.example.apoiodigital.Service.AudioService;
import com.example.apoiodigital.Service.RequisicaoService;

import java.io.File;

import okhttp3.Call;

public class ModalRepository {

    private final AtalhoService atalhoService;
    private final RequisicaoService requisicaoService;
    private final AudioService audioService;

    public ModalRepository(AtalhoService atalhoService, RequisicaoService requisicaoService, AudioService audioService) {
        this.atalhoService = atalhoService;
        this.requisicaoService = requisicaoService;
        this.audioService = audioService;
    }

    public Call getAllAtalhos(String usuarioID){
        return atalhoService.getAll(usuarioID);
    }

    public Call initAtalho(String atalhoID){
        return atalhoService.initByID(atalhoID);
    }

    public Call postRequisicao(RequisicaoInput requisicaoInput){
        return requisicaoService.post(requisicaoInput);
    }

    public Call requestSTT(File filepath){
        return audioService.stt(filepath);
    }
}
