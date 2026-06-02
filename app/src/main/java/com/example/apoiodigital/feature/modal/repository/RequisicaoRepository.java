package com.example.apoiodigital.feature.modal.repository;

import com.example.apoiodigital.core.Network.RetrofitClient;
import com.example.apoiodigital.feature.modal.service.ModalApiService;
import com.example.apoiodigital.feature.modal.data.RequisicaoInput;
import com.example.apoiodigital.feature.modal.data.RequisicaoResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequisicaoRepository {

    private ModalApiService modalApiService;

    public RequisicaoRepository(){
        this.modalApiService = RetrofitClient.getRetrofitInstance().create(ModalApiService.class);
    }

    public void postRequisicao(RequisicaoInput requisicaoInput, RequisicaoCallBack callback) {

        modalApiService.enviarRequisicao(requisicaoInput).enqueue(new Callback<RequisicaoResponse>() {

            @Override
            public void onResponse(Call<RequisicaoResponse> call, Response<RequisicaoResponse> response) {
                if(response.isSuccessful()){
                    callback.onSuccess(response.body());
                }else {
                    callback.onError("Erro ao enviar requisição");
                }
            }

            @Override
            public void onFailure(Call<RequisicaoResponse> call, Throwable t) {
                callback.onError("Erro ao enviar requisição");
            }
        });
    }

    public interface RequisicaoCallBack{
        void onSuccess(RequisicaoResponse response);
        void onError(String error);
    }

}
