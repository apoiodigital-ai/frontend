package com.example.apoiodigital.core.Network;

import com.example.apoiodigital.Globals;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {

    private static Globals globals = new Globals();
    private static Retrofit retrofit;
    private static Retrofit slowRetrofit;
    private static Retrofit sttRetrofit;

    private static final String BASE_URL     = "http://" + globals.BaseUrl + ":8080/";
    private static final String STT_BASE_URL = "http://" + globals.BaseUrl + ":5000/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = createRetorfit(BASE_URL, 10, 10, 10);
        }
        return retrofit;
    }

    public static Retrofit getFastRetrofitInstance() {
        if (retrofit == null) {
            retrofit = createRetorfit(BASE_URL, 3, 3, 3);
        }
        return retrofit;
    }

    /** Instância com timeouts longos (500 s) para requisições/respostas de IA. */
    public static Retrofit getSlowRetrofitInstance() {
        if (slowRetrofit == null) {
            slowRetrofit = createSlowRetrofit();
        }
        return slowRetrofit;
    }

    /** Instância apontando para o serviço STT (porta 5000). */
    public static Retrofit getSttRetrofitInstance() {
        if (sttRetrofit == null) {
            sttRetrofit = createRetorfit(STT_BASE_URL, 20, 20, 20);
        }
        return sttRetrofit;
    }

    public static Retrofit createRetorfit(String baseUrl, int connectTimeout, int writeTimeout, int readTimeout) {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();
    }

    /**
     * Instância lenta com ScalarsConverterFactory registrado antes do Gson,
     * necessário para endpoints que retornam String pura (ex: resposta/exigir).
     */
    private static Retrofit createSlowRetrofit() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(500, TimeUnit.SECONDS)
                .writeTimeout(500, TimeUnit.SECONDS)
                .readTimeout(500, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();
    }

    @Deprecated
    public static Retrofit createRetorfit(int connectTimeout, int writeTimeout, int readTimeout) {
        return createRetorfit(BASE_URL, connectTimeout, writeTimeout, readTimeout);
    }
}
