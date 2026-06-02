package com.example.apoiodigital.feature.modal.service;

import com.example.apoiodigital.core.tables.atalho.Atalho;
import com.example.apoiodigital.core.tables.requisicao.Requisicao;
import com.example.apoiodigital.feature.modal.data.RequisicaoInput;
import com.example.apoiodigital.feature.modal.data.RequisicaoResponse;
import com.example.apoiodigital.feature.modal.data.STTResponse;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ModalApiService {

    @POST("requisicao/enviar")
    Call<RequisicaoResponse> enviarRequisicao(@Body RequisicaoInput requisicaoInput);

    @GET("atalho/carregar")
    Call<List<Atalho>> carregarAtalhos(@Query("id_usuario") String idUsuario);

    @POST("atalho/iniciar")
    Call<Requisicao> iniciarAtalho(@Query("id_atalho") String idAtalho);

    @Multipart
    @POST("stt")
    Call<STTResponse> stt(@Part MultipartBody.Part audio);

}
