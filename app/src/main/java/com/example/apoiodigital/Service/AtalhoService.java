package com.example.apoiodigital.Service;

import com.example.apoiodigital.Globals;

import okhttp3.Call;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class AtalhoService {

    private String baseUrl = new Globals().BaseUrl;

    public Call getAll(String usuarioID) {

        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://" + baseUrl + ":8080/atalho/carregar").newBuilder();
        urlBuilder.addQueryParameter("id_usuario", usuarioID);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        return client.newCall(request);

    }

    public Call initByID(String atalhoID){
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://" + baseUrl + ":8080/atalho/iniciar").newBuilder();
        urlBuilder.addQueryParameter("id_atalho", atalhoID);
        String url = urlBuilder.build().toString();
        RequestBody emptyBody = RequestBody.create(null, new byte[0]);

        Request request = new Request.Builder()
                .url(url)
                .method("POST", emptyBody)
                .header("Content-Length", "0")
                .build();

        return client.newCall(request);
    }

}
