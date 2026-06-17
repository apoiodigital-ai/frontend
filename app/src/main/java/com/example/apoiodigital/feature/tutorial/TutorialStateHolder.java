package com.example.apoiodigital.feature.tutorial;

import androidx.lifecycle.MutableLiveData;

import com.example.apoiodigital.feature.tutorial.data.AdditionalInfoDTO;
import com.example.apoiodigital.feature.tutorial.data.BaseDataToIADTO;
import com.example.apoiodigital.feature.tutorial.data.ChecksInformationNeedsResponseDTO;
import com.example.apoiodigital.feature.tutorial.data.FindBestAnswerResponseDTO;

public class TutorialStateHolder {
    private static TutorialStateHolder instance;
    private final MutableLiveData<ChecksInformationNeedsResponseDTO> checksInformationNeedsResponse = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isRespostaLoading = new MutableLiveData<>();
    private final MutableLiveData<FindBestAnswerResponseDTO> answerResponse = new MutableLiveData<>();
    private final MutableLiveData<AdditionalInfoDTO> additionalInfo = new MutableLiveData<>();
    private final MutableLiveData<BaseDataToIADTO> baseDataToIA = new MutableLiveData<>();

    private TutorialStateHolder() {}

    public static synchronized TutorialStateHolder getInstance() {
        if (instance == null) {
            instance = new TutorialStateHolder();
        }
        return instance;
    }

    public MutableLiveData<FindBestAnswerResponseDTO> getAnswerResponse() {
        return answerResponse;
    }
    public MutableLiveData<BaseDataToIADTO> getBaseDataToIA() {
        return baseDataToIA;

    }

    public MutableLiveData<AdditionalInfoDTO> getAdditionalInfo() {
        return additionalInfo;
    }



    public MutableLiveData<ChecksInformationNeedsResponseDTO> getChecksInformationNeedsResponse() {
        return checksInformationNeedsResponse;
    }

    public MutableLiveData<Boolean> getIsRespostaLoading() {
        return isRespostaLoading;
    }
}
