package com.example.apoiodigital.feature.modal.service;

import com.example.apoiodigital.feature.api.ApiService;
import com.example.apoiodigital.feature.modal.data.STTResponse;
import com.example.apoiodigital.core.Network.RetrofitClient;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class AudioService {

    private final ModalApiService apiService =
            RetrofitClient.getSttRetrofitInstance().create(ModalApiService.class);

    public Call<STTResponse> stt(File file) {
        RequestBody fileBody = RequestBody.create(file, MediaType.parse("audio/mp4"));
        MultipartBody.Part part = MultipartBody.Part.createFormData("audio", file.getName(), fileBody);
        return apiService.stt(part);
    }
}
