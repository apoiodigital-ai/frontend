package com.example.apoiodigital.feature.modal.repository;

import com.example.apoiodigital.core.Network.RetrofitClient;
import com.example.apoiodigital.feature.modal.service.AudioService;
import com.example.apoiodigital.feature.modal.data.STTResponse;
import com.example.apoiodigital.feature.modal.service.ModalApiService;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AudioRepository {

    private ModalApiService modalApiService;

    public AudioRepository() {

        this.modalApiService = RetrofitClient.getSttRetrofitInstance().create(ModalApiService.class);

    }


    public void requestSTT(File filepath, AudioCallBack callback){

        RequestBody fileBody = RequestBody.create(filepath, MediaType.parse("audio/mp4"));
        MultipartBody.Part part = MultipartBody.Part.createFormData("audio", filepath.getName(), fileBody);
        modalApiService.stt(part).enqueue(new Callback<STTResponse>() {
            @Override
            public void onResponse(Call<STTResponse> call, Response<STTResponse> response) {
                if(response.isSuccessful()){
                    callback.onSuccess(response.body());
                }else {
                    callback.onError("Erro ao transformar texto");
                }

            };
            @Override
            public void onFailure(Call<STTResponse> call, Throwable t) {
                callback.onError("Erro ao enviar requisição");
            }
        });

    }

    public interface AudioCallBack{
        void onSuccess(STTResponse response);
        void onError(String error);
    }


}
