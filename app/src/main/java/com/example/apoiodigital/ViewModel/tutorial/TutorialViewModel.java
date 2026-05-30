package com.example.apoiodigital.ViewModel.tutorial;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.apoiodigital.Dto.FindBestAnswerResponseDTO;
import com.example.apoiodigital.Model.FindBestAnswerRequestDTO;
import com.example.apoiodigital.Repository.TutorialRepository;
import com.example.apoiodigital.Service.RespostaService;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TutorialViewModel {

    private final RespostaService respostaService = new RespostaService();
    private final TutorialRepository tutorialRepository = new TutorialRepository(respostaService);
    private final MutableLiveData<Boolean> isRespostaLoading = new MutableLiveData<>();
    private FindBestAnswerResponseDTO dataResponse;
    private final String TAG = "TutorialViewModelLOGE";

    private final Context context;

    public TutorialViewModel(Context context) {
        this.context = context;
    }

    public void getResponseIA(FindBestAnswerRequestDTO requestDTO) {
        isRespostaLoading.postValue(true);
        tutorialRepository.getIaMessage(requestDTO).enqueue(new Callback<FindBestAnswerResponseDTO>() {
            @Override
            public void onResponse(Call<FindBestAnswerResponseDTO> call, Response<FindBestAnswerResponseDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    dataResponse = response.body();
                    Log.e(TAG, "onResponse: " + dataResponse);

                    Intent responseIntent = new Intent("com.example.apoiodigital.GET_RESPONSE_IA");

                    Gson gson = new Gson();
                    String responseString = gson.toJson(dataResponse);
                    responseIntent.putExtra("responseIA", responseString);
                    context.sendBroadcast(responseIntent);
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
}
