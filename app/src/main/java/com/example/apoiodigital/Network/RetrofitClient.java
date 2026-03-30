package com.example.apoiodigital.Network;

import com.example.apoiodigital.Globals;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Globals globals = new Globals();
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://" + globals.BaseUrl + ":8080/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = createRetorfit(10, 10, 10);
        }
        return retrofit;
    }

    public static Retrofit getFastRetrofitInstance(){
        if (retrofit == null) {
            retrofit = createRetorfit(3, 3, 3);
        }
        return retrofit;
    }

    public static Retrofit createRetorfit(int connetcTimeout, int writeTimeout, int readTimeout) {
        OkHttpClient httpClient = new OkHttpClient
                .Builder()
                .connectTimeout(connetcTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();
    }

}
