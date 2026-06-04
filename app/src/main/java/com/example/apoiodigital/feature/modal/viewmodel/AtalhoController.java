package com.example.apoiodigital.feature.modal.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.example.apoiodigital.core.tables.atalho.Atalho;
import com.example.apoiodigital.core.tables.requisicao.Requisicao;
import com.example.apoiodigital.feature.modal.repository.AtalhoRepository;
import com.example.apoiodigital.feature.modal.state.GetAtalhosState;
import com.example.apoiodigital.feature.modal.state.InitAtalhoState;

import java.util.List;

public class AtalhoController {

    private final AtalhoRepository atalhoRepository;
    private MutableLiveData<Requisicao> atalhoResponse = new MutableLiveData<>();
    private MutableLiveData<List<Atalho>> getAtalhosResponse = new MutableLiveData<>();
    private MutableLiveData<InitAtalhoState> initAtalhoState = new MutableLiveData<>();
    private MutableLiveData<GetAtalhosState> getAtalhosState = new MutableLiveData<>();

    public MutableLiveData<GetAtalhosState> getGetAtalhosState() {
        return getAtalhosState;
    }

    public MutableLiveData<List<Atalho>> getGetAtalhosResponse() {
        return getAtalhosResponse;
    }

    public AtalhoController() {
        this.atalhoRepository = new AtalhoRepository();
    }

    public MutableLiveData<Requisicao> getAtalhoResponse() {
        return atalhoResponse;
    }

    public MutableLiveData<InitAtalhoState> getInitAtalhoState() {
        return initAtalhoState;
    }


    public void iniciarAtalho(String idAtalho) {
        initAtalhoState.postValue(new InitAtalhoState(true, null, false));
        atalhoRepository.iniciarAtalho(idAtalho, new AtalhoRepository.AtalhoCallBack() {
            @Override
            public void onSuccess(Requisicao requisicao) {
                atalhoResponse.postValue(requisicao);
                initAtalhoState.postValue(new InitAtalhoState(false, null, true));
            }
            @Override
            public void onError(String error) {
                initAtalhoState.postValue(new InitAtalhoState(false, error, false));
            }
        });
    }

    public void carregarAtalhos(String usuarioID){
        getAtalhosState.postValue(new GetAtalhosState(true, null, false));
        atalhoRepository.carregarAtalhos(usuarioID, new AtalhoRepository.GetAtalhosCallBack() {
            @Override
            public void onSuccess(List<Atalho> atalhos) {
                getAtalhosState.postValue(new GetAtalhosState(false, null, true));

            }
            @Override
            public void onError(String error) {
                getAtalhosState.postValue(new GetAtalhosState(false, error, false));
            }
        });
    }



}
