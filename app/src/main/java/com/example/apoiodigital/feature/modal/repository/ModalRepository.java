package com.example.apoiodigital.feature.modal.repository;

import com.example.apoiodigital.feature.modal.service.AudioService;
import com.example.apoiodigital.core.tables.atalho.Atalho;
import com.example.apoiodigital.core.tables.requisicao.Requisicao;
import com.example.apoiodigital.feature.modal.data.RequisicaoInput;
import com.example.apoiodigital.feature.modal.data.RequisicaoResponse;
import com.example.apoiodigital.feature.modal.data.STTResponse;
import com.example.apoiodigital.core.tables.atalho.AtalhoService;
import com.example.apoiodigital.core.tables.requisicao.RequisicaoService;

import java.io.File;
import java.util.List;

import retrofit2.Call;

public class ModalRepository {

    private final AtalhoService atalhoService;
    private final RequisicaoService requisicaoService;
    private final AudioService audioService;

    public ModalRepository(AtalhoService atalhoService, RequisicaoService requisicaoService, AudioService audioService) {
        this.atalhoService = atalhoService;
        this.requisicaoService = requisicaoService;
        this.audioService = audioService;
    }

    public Call<List<Atalho>> getAllAtalhos(String usuarioID) {
        return atalhoService.getAll(usuarioID);
    }

    public Call<Requisicao> initAtalho(String atalhoID) {
        return atalhoService.initByID(atalhoID);
    }

    public Call<RequisicaoResponse> postRequisicao(RequisicaoInput requisicaoInput) {
        return requisicaoService.post(requisicaoInput);
    }

    public Call<STTResponse> requestSTT(File filepath) {
        return audioService.stt(filepath);
    }
}
