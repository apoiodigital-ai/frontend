package com.example.apoiodigital.ViewModel.modal;

import android.content.Intent;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ModalViewModel extends ViewModel {

    private final AtalhoService atalhoService = new AtalhoService();
    private final RequisicaoService requisicaoService = new RequisicaoService();
    private final AudioService audioService = new AudioService();
    private final ModalRepository modalRepository = new ModalRepository(atalhoService, requisicaoService, audioService);

    private MutableLiveData<List<Atalho>> atalhos = new MutableLiveData<>();;
    private MutableLiveData<String> prompt = new MutableLiveData<>();
    private MutableLiveData<Requisicao> atalhoResponse = new MutableLiveData<>();
    private MutableLiveData<Boolean> isAtalhosLoading =new MutableLiveData<>();
    private MutableLiveData<Boolean> requisicaoSendLoading = new MutableLiveData<>();
    private MutableLiveData<String> sttReturn = new MutableLiveData<>();
    private MutableLiveData<String> appRequest = new MutableLiveData<>();
    private MutableLiveData<RequisicaoResponse> requisicaoResponse = new MutableLiveData<>();

    private Gson gson = new Gson();

    public MutableLiveData<Boolean> getRequisicaoSendLoading(){
        if(requisicaoSendLoading == null){
            requisicaoSendLoading = new MutableLiveData<>();
        }
        return requisicaoSendLoading;
    }

    public MutableLiveData<Boolean> getIsAtalhosLoading(){
        if(isAtalhosLoading == null){
            isAtalhosLoading = new MutableLiveData<>();
        }
        return isAtalhosLoading;
    }

    public MutableLiveData<List<Atalho>> getAtalhos(){
        if(atalhos == null){
            atalhos = new MutableLiveData<List<Atalho>>();
        }
        return atalhos;
    }

    public MutableLiveData<RequisicaoResponse> getRequisicaoResponse(){
        if(requisicaoResponse == null){
            requisicaoResponse = new MutableLiveData<RequisicaoResponse>();
        }
        return requisicaoResponse;
    }

    public MutableLiveData<Requisicao> getAtalhoResponse(){
        if(atalhoResponse == null){
            atalhoResponse = new MutableLiveData<Requisicao>();
        }
        return atalhoResponse;
    }

    public MutableLiveData<String> getSttReturn(){
        if(sttReturn == null){
            sttReturn = new MutableLiveData<String>();
        }
        return sttReturn;
    }

    public void carregarAtalhos(String usuarioID){

        isAtalhosLoading.postValue(true);
        var call = modalRepository.getAllAtalhos(usuarioID);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("AAA", "onFailure: " + e.getMessage() );
                atalhos.postValue(null);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                var itemType = TypeToken.getParameterized(List.class, Atalho.class);
                List<Atalho> atalhosFromRequest = gson.fromJson(response.body().string(), itemType.getType());

                atalhos.postValue(atalhosFromRequest);

            }
        });

    }

    public void iniciarAtalho(String atalhoID){


        requisicaoSendLoading.postValue(true);

        var call = modalRepository.initAtalho(atalhoID);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                atalhoResponse.postValue(null);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Requisicao requisicaoFromRequest = gson.fromJson(response.body().string(), Requisicao.class);
                atalhoResponse.postValue(requisicaoFromRequest);

            }
        });

    }

    public void enviarRequisicao(RequisicaoInput requisicaoInput){

        requisicaoSendLoading.postValue(true);

        var call = modalRepository.postRequisicao(requisicaoInput);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                requisicaoResponse.postValue(null);
                Log.e("GetElementService", "onChanged: " + e.getMessage());

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                RequisicaoResponse requisicaoFromRequest = gson.fromJson(response.body().string(), RequisicaoResponse.class);
                Log.e("FOWAOFWKAKFKWFW", "requisicaoResponse: AQUI NO ONRESPONSE" );

                requisicaoResponse.postValue(requisicaoFromRequest);

            }
        });

    }

    public void transformarAudioParaTexto(File filepath){

        var call = modalRepository.requestSTT(filepath);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                sttReturn.postValue(null);
                Log.e("STTResponse", "onFailure: " + e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                STTResponse sttResponse = gson.fromJson(response.body().string(), STTResponse.class);
                sttReturn.postValue(sttResponse.getTranscription());
//                Log.e("STTResponse", "onResponse: " + response.body());
                if(filepath.exists()) filepath.delete();
            }
        });

    }


}
