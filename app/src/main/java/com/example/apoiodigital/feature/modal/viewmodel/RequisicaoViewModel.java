package com.example.apoiodigital.feature.modal.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.apoiodigital.feature.modal.data.RequisicaoInput;
import com.example.apoiodigital.feature.modal.data.RequisicaoResponse;
import com.example.apoiodigital.feature.modal.repository.RequisicaoRepository;
import com.example.apoiodigital.feature.modal.state.RequisicaoState;

public class RequisicaoViewModel extends ViewModel {

    private final RequisicaoRepository requisicaoRepository;
    private MutableLiveData<RequisicaoResponse> requisicaoResponse = new MutableLiveData<>();
    private MutableLiveData<RequisicaoState> state = new MutableLiveData<>();

    public MutableLiveData<RequisicaoResponse> getRequisicaoResponse() {
        return requisicaoResponse;
    }

    public MutableLiveData<RequisicaoState> getState() {
        return state;
    }

    public RequisicaoViewModel() {
        this.requisicaoRepository = new RequisicaoRepository();
    }

    public void enviarRequisicao(RequisicaoInput requisicaoInput) {
        state.setValue(new RequisicaoState(true, null, false));

        requisicaoRepository.postRequisicao(requisicaoInput, new RequisicaoRepository.RequisicaoCallBack() {
            @Override
            public void onSuccess(RequisicaoResponse requisicao) {
                requisicaoResponse.postValue(requisicao);
                state.postValue(new RequisicaoState(false, null, true));
            }
            @Override
            public void onError(String error) {

                state.postValue(new RequisicaoState(false, error, false));
            }
        });
    }
}
