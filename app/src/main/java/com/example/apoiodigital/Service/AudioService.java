package com.example.apoiodigital.Service;

import com.example.apoiodigital.Globals;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class AudioService {

    private String baseUrl = new Globals().BaseUrl;
    public Call stt(File file){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .build();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://" + baseUrl + ":5000/stt").newBuilder();

        MediaType mediaType = MediaType.parse("audio/mp4");
        RequestBody fileBody = RequestBody.create(file, mediaType);

        String url = urlBuilder.build().toString();

        var body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("audio", file.getName(), fileBody)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("content-type", "multipart/form-data")
                .build();

        return client.newCall(request);

    }

}
