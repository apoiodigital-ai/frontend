package com.example.apoiodigital.feature.tutorial;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.apoiodigital.feature.tutorial.data.AdditionalInfoDTO;
import com.example.apoiodigital.feature.tutorial.data.BaseDataToIADTO;
import com.example.apoiodigital.feature.tutorial.data.ChecksInformationNeedsRequestDTO;
import com.example.apoiodigital.feature.tutorial.data.ChecksInformationNeedsResponseDTO;
import com.example.apoiodigital.feature.tutorial.data.FindBestAnswerResponseDTO;
import com.example.apoiodigital.feature.tutorial.data.FindBestAnswerRequestDTO;
import com.example.apoiodigital.feature.tutorial.repository.TutorialRepository;
import com.example.apoiodigital.core.tables.resposta.RespostaService;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TutorialViewModel {

    private final RespostaService respostaService = new RespostaService();
    private final TutorialRepository tutorialRepository = new TutorialRepository(respostaService);
    private final MutableLiveData<Boolean> isRespostaLoading = TutorialStateHolder.getInstance().getIsRespostaLoading();
    private final MutableLiveData<ChecksInformationNeedsResponseDTO> checksInformationNeedsResponse = TutorialStateHolder.getInstance().getChecksInformationNeedsResponse();
    private final MutableLiveData<FindBestAnswerResponseDTO> answerResponse = TutorialStateHolder.getInstance().getAnswerResponse();

    private final MutableLiveData<BaseDataToIADTO> baseDataToIA = TutorialStateHolder.getInstance().getBaseDataToIA();
    private final MutableLiveData<AdditionalInfoDTO> additionalInfo = TutorialStateHolder.getInstance().getAdditionalInfo();

    private final String TAG = "TutorialViewModelLOGE";

    private final Context context;

    public MutableLiveData<ChecksInformationNeedsResponseDTO> getChecksInformationNeedsResponse() {
        return checksInformationNeedsResponse;

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

    public TutorialViewModel(Context context) {
        this.context = context;
    }

    //TODO: needs to update in GetElementService
    public void getResponseIA(FindBestAnswerRequestDTO requestDTO) {
        isRespostaLoading.postValue(true);
        tutorialRepository.getIaMessage(requestDTO).enqueue(new Callback<FindBestAnswerResponseDTO>() {
            @Override
            public void onResponse(Call<FindBestAnswerResponseDTO> call, Response<FindBestAnswerResponseDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    answerResponse.postValue(response.body());
//                    dataResponse = response.body();
//                    Log.e(TAG, "onResponse: " + dataResponse);
//
//                    Intent responseIntent = new Intent("com.example.apoiodigital.GET_RESPONSE_IA");
//
//                    Gson gson = new Gson();
//                    String responseString = gson.toJson(dataResponse);
//                    responseIntent.putExtra("responseIA", responseString);
//                    context.sendBroadcast(responseIntent);
                } else {
                    Log.e(TAG, "Erro: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<FindBestAnswerResponseDTO> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }

    public void checkInformationNeeds(ChecksInformationNeedsRequestDTO requestDTO){
        isRespostaLoading.postValue(true);

        tutorialRepository.validarNecessidadeInformacoes(requestDTO).enqueue(new Callback<ChecksInformationNeedsResponseDTO>() {
            @Override
            public void onResponse(Call<ChecksInformationNeedsResponseDTO> call, Response<ChecksInformationNeedsResponseDTO> response) {
                Log.e(TAG, "CHECK FEATURE - onResponse: " + response.body());

                if(response.body() != null && response.isSuccessful()){
                    isRespostaLoading.postValue(false);

                    ChecksInformationNeedsResponseDTO dto = response.body();
                    dto.setContexto(requestDTO.getContexto());

                    checksInformationNeedsResponse.postValue(response.body());
                    Log.e(TAG, "CHECK FEATURE - onResponse: " + response.body().getDescricao_duvida());

                }else if (response.body() == null){
                    checksInformationNeedsResponse.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ChecksInformationNeedsResponseDTO> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }
}
