package com.example.apoiodigital.ViewModel.tutorial;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.apoiodigital.Model.CryptedRequestIA;
import com.example.apoiodigital.Repository.TutorialRepository;
import com.example.apoiodigital.Service.RespostaService;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class TutorialViewModel {

    private RespostaService respostaService = new RespostaService();
    private TutorialRepository tutorialRepository = new TutorialRepository(respostaService);
    private MutableLiveData<Boolean> isRespostaLoading = new MutableLiveData<>();
    private String dataResponse;
    private String TAG = "TutorialViewModelLOGE";

    private final Context context;

    public TutorialViewModel(Context context) {
        this.context = context;
    }

    public void getResponseIA(CryptedRequestIA requestDTO){
        isRespostaLoading.postValue(true);
        var call = tutorialRepository.getIaMessage(requestDTO);

        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "onFailure: " + e.getMessage() );
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseBody = response.body().string();

                if(response.isSuccessful()) {
//                    Log.e(TAG, "Response "+ responseBody);
                    dataResponse = responseBody;
                    Log.e(TAG, "onResponse: " + dataResponse );

                    Intent responseIntent = new Intent("com.example.apoiodigital.GET_RESPONSE_IA");
                    responseIntent.putExtra("responseIA", dataResponse);

                    context.sendBroadcast(responseIntent);

                }else{
//                    Log.e(TAG, response.message().toString() );
                    Log.e(TAG, "Erro: " +response.code());
                }
            }
        });



    }

}
