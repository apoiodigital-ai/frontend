package com.example.apoiodigital.feature.modal.repository;

import com.example.apoiodigital.core.Network.RetrofitClient;
import com.example.apoiodigital.core.tables.atalho.Atalho;
import com.example.apoiodigital.core.tables.requisicao.Requisicao;
import com.example.apoiodigital.feature.modal.service.ModalApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AtalhoRepository {

    private ModalApiService modalApiService;

    public AtalhoRepository(){
        this.modalApiService = RetrofitClient.getRetrofitInstance().create(ModalApiService.class);
    }

    public void iniciarAtalho(String idAtalho, AtalhoCallBack callback){
        modalApiService.iniciarAtalho(idAtalho).enqueue(new Callback<Requisicao>() {
            @Override
            public void onResponse(Call<Requisicao> call, Response<Requisicao> response) {
                if(response.isSuccessful() && response.body() != null){
                    callback.onSuccess(response.body());
                }else {
                    callback.onError("Erro ao iniciar atalho");
                }
            }

            @Override
            public void onFailure(Call<Requisicao> call, Throwable t) {
                callback.onError("Erro ao iniciar atalho");
            }
        });
    }

    public void carregarAtalhos(String usuarioID, GetAtalhosCallBack callBack){
        modalApiService.carregarAtalhos(usuarioID).enqueue(new Callback<List<Atalho>>() {
            @Override
            public void onResponse(Call<List<Atalho>> call, Response<List<Atalho>> response) {
                if(response.isSuccessful() && response.body() != null){
                    callBack.onSuccess(response.body());
                }else {
                    callBack.onError("Erro ao carregar os atalhos!");
                }
        }
            @Override
            public void onFailure(Call<List<Atalho>> call, Throwable t) {
                callBack.onError("Erro ao carregar os atalhos!");
            }
        });
    }

    public interface AtalhoCallBack{
        void onSuccess(Requisicao requisicao);
        void onError(String error);
    }
    public interface GetAtalhosCallBack{
        void onSuccess(List<Atalho> atalhos);
        void onError(String error);
    }
}
