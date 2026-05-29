package com.example.apoiodigital.ViewModel.modal;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apoiodigital.Model.Atalho;
import com.example.apoiodigital.Model.Requisicao;
import com.example.apoiodigital.Model.RequisicaoInput;
import com.example.apoiodigital.Model.RequisicaoResponse;
import com.example.apoiodigital.Model.STTResponse;
import com.example.apoiodigital.Repository.ModalRepository;
import com.example.apoiodigital.Service.AtalhoService;
import com.example.apoiodigital.Service.AudioService;
import com.example.apoiodigital.Service.RequisicaoService;

import java.io.File;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModalViewModel extends ViewModel {

    private final AtalhoService atalhoService = new AtalhoService();
    private final RequisicaoService requisicaoService = new RequisicaoService();
    private final AudioService audioService = new AudioService();
    private final ModalRepository modalRepository = new ModalRepository(atalhoService, requisicaoService, audioService);

    private MutableLiveData<List<Atalho>> atalhos = new MutableLiveData<>();
    private MutableLiveData<String> prompt = new MutableLiveData<>();
    private MutableLiveData<Requisicao> atalhoResponse = new MutableLiveData<>();
    private MutableLiveData<Boolean> isAtalhosLoading = new MutableLiveData<>();
    private MutableLiveData<Boolean> requisicaoSendLoading = new MutableLiveData<>();
    private MutableLiveData<String> sttReturn = new MutableLiveData<>();
    private MutableLiveData<String> appRequest = new MutableLiveData<>();
    private MutableLiveData<RequisicaoResponse> requisicaoResponse = new MutableLiveData<>();

    public MutableLiveData<Boolean> getRequisicaoSendLoading() {
        if (requisicaoSendLoading == null) requisicaoSendLoading = new MutableLiveData<>();
        return requisicaoSendLoading;
    }

    public MutableLiveData<Boolean> getIsAtalhosLoading() {
        if (isAtalhosLoading == null) isAtalhosLoading = new MutableLiveData<>();
        return isAtalhosLoading;
    }

    public MutableLiveData<List<Atalho>> getAtalhos() {
        if (atalhos == null) atalhos = new MutableLiveData<>();
        return atalhos;
    }

    public MutableLiveData<RequisicaoResponse> getRequisicaoResponse() {
        if (requisicaoResponse == null) requisicaoResponse = new MutableLiveData<>();
        return requisicaoResponse;
    }

    public MutableLiveData<Requisicao> getAtalhoResponse() {
        if (atalhoResponse == null) atalhoResponse = new MutableLiveData<>();
        return atalhoResponse;
    }

    public MutableLiveData<String> getSttReturn() {
        if (sttReturn == null) sttReturn = new MutableLiveData<>();
        return sttReturn;
    }

    public void carregarAtalhos(String usuarioID) {
        isAtalhosLoading.postValue(true);
        modalRepository.getAllAtalhos(usuarioID).enqueue(new Callback<List<Atalho>>() {
            @Override
            public void onResponse(Call<List<Atalho>> call, Response<List<Atalho>> response) {
                atalhos.postValue(response.isSuccessful() ? response.body() : null);
            }

            @Override
            public void onFailure(Call<List<Atalho>> call, Throwable t) {
                Log.e("ModalViewModel", "carregarAtalhos onFailure: " + t.getMessage());
                atalhos.postValue(null);
            }
        });
    }

    public void iniciarAtalho(String atalhoID) {
        requisicaoSendLoading.postValue(true);
        modalRepository.initAtalho(atalhoID).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // atalhoResponse mantido para compatibilidade; sem body neste endpoint
                atalhoResponse.postValue(response.isSuccessful() ? new Requisicao() : null);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                atalhoResponse.postValue(null);
            }
        });
    }

    public void enviarRequisicao(RequisicaoInput requisicaoInput) {
        requisicaoSendLoading.postValue(true);
        modalRepository.postRequisicao(requisicaoInput).enqueue(new Callback<RequisicaoResponse>() {
            @Override
            public void onResponse(Call<RequisicaoResponse> call, Response<RequisicaoResponse> response) {
                Log.e("ModalViewModel", "enviarRequisicao onResponse");
                requisicaoResponse.postValue(response.isSuccessful() ? response.body() : null);
            }

            @Override
            public void onFailure(Call<RequisicaoResponse> call, Throwable t) {
                Log.e("ModalViewModel", "enviarRequisicao onFailure: " + t.getMessage());
                requisicaoResponse.postValue(null);
            }
        });
    }

    public void transformarAudioParaTexto(File filepath) {
        modalRepository.requestSTT(filepath).enqueue(new Callback<STTResponse>() {
            @Override
            public void onResponse(Call<STTResponse> call, Response<STTResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    sttReturn.postValue(response.body().getTranscription());
                } else {
                    sttReturn.postValue(null);
                }
                if (filepath.exists()) filepath.delete();
            }

            @Override
            public void onFailure(Call<STTResponse> call, Throwable t) {
                Log.e("ModalViewModel", "transformarAudioParaTexto onFailure: " + t);
                sttReturn.postValue(null);
            }
        });
    }
}
