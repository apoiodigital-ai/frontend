package com.example.apoiodigital.feature.modal.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.example.apoiodigital.feature.modal.repository.AudioRepository;
import com.example.apoiodigital.feature.modal.service.AudioService;
import com.example.apoiodigital.core.tables.atalho.Atalho;
import com.example.apoiodigital.core.tables.requisicao.Requisicao;
import com.example.apoiodigital.feature.modal.data.STTResponse;
import com.example.apoiodigital.feature.modal.state.AudioState;

import java.io.File;
import java.util.List;

public class AudioController {

    private final AudioService audioService = new AudioService();
    private final AudioRepository audioRepository = new AudioRepository();
    private MutableLiveData<AudioState> state = new MutableLiveData<>();
    private MutableLiveData<List<Atalho>> atalhos = new MutableLiveData<>();
    private MutableLiveData<Requisicao> atalhoResponse = new MutableLiveData<>();
    private MutableLiveData<Boolean> isAtalhosLoading = new MutableLiveData<>();
    private MutableLiveData<String> sttReturn = new MutableLiveData<>();

    public MutableLiveData<Boolean> getIsAtalhosLoading() {
        if (isAtalhosLoading == null) isAtalhosLoading = new MutableLiveData<>();
        return isAtalhosLoading;
    }

    public MutableLiveData<List<Atalho>> getAtalhos() {
        if (atalhos == null) atalhos = new MutableLiveData<>();
        return atalhos;
    }

    public MutableLiveData<Requisicao> getAtalhoResponse() {
        if (atalhoResponse == null) atalhoResponse = new MutableLiveData<>();
        return atalhoResponse;
    }

    public MutableLiveData<String> getSttReturn() {
        if (sttReturn == null) sttReturn = new MutableLiveData<>();
        return sttReturn;
    }

    public void transformarAudioParaTexto(File filepath) {
        state.postValue(new AudioState(true, null, false));
        audioRepository.requestSTT(filepath, new AudioRepository.AudioCallBack() {
            @Override
            public void onSuccess(STTResponse response) {
                state.postValue(new AudioState(false, null, true));
                sttReturn.postValue(response.getTranscription());
            }
            @Override
            public void onError(String error) {
                state.postValue(new AudioState(false, error, false));
            }
        });
    }
}
