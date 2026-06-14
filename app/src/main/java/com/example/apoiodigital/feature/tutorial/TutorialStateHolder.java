package com.example.apoiodigital.feature.tutorial;

import androidx.lifecycle.MutableLiveData;
import com.example.apoiodigital.feature.tutorial.data.ChecksInformationNeedsResponseDTO;

public class TutorialStateHolder {
    private static TutorialStateHolder instance;
    private final MutableLiveData<ChecksInformationNeedsResponseDTO> checksInformationNeedsResponse = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isRespostaLoading = new MutableLiveData<>();

    private TutorialStateHolder() {}

    public static synchronized TutorialStateHolder getInstance() {
        if (instance == null) {
            instance = new TutorialStateHolder();
        }
        return instance;
    }

    public MutableLiveData<ChecksInformationNeedsResponseDTO> getChecksInformationNeedsResponse() {
        return checksInformationNeedsResponse;
    }

    public MutableLiveData<Boolean> getIsRespostaLoading() {
        return isRespostaLoading;
    }
}
