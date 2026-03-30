package com.example.apoiodigital.Service;

import com.example.apoiodigital.Globals;
import com.example.apoiodigital.Model.RequisicaoInput;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class RequisicaoService {

    private String baseUrl = new Globals().BaseUrl;
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private Gson gson = new Gson();

    public Call post(RequisicaoInput requisicaoInput){

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(500, TimeUnit.SECONDS)
                .readTimeout(500, TimeUnit.SECONDS)
                .writeTimeout(500, TimeUnit.SECONDS)
                .build();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://" + baseUrl + ":8080/requisicao/enviar").newBuilder();


        String url = urlBuilder.build().toString();
        var jsonBody = gson.toJson(requisicaoInput);

        RequestBody body = RequestBody.create(jsonBody, JSON);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        return client.newCall(request);

    }

}
