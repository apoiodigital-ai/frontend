package com.example.apoiodigital.Service;

import com.example.apoiodigital.Globals;
import com.example.apoiodigital.Model.CryptedRequestIA;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class RespostaService {

    private String baseUrl = new Globals().BaseUrl;
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private String TAG = "APIServiceGetElement";



//    public void saveinFile(String json){
//        var baseName = "jsonOutput";
//        var extension = "json";
//        File dir = context.getExternalFilesDir(null);
//        int index = 0;
//        File file;
//
//        do {
//            String fileName = (index == 0) ? baseName + "." + extension
//                    : baseName + index + "." + extension;
//            file = new File(dir, fileName);
//            index++;
//        } while (file.exists());
//
//        try (FileWriter writer = new FileWriter(file)) {
//            writer.write(json);
//            Log.d("DEBUG_FILE", "Arquivo salvo: " + file.getAbsolutePath());
//        } catch (IOException e) {
//            Log.e("DEBUG_FILE", "Erro ao salvar o arquivo", e);
//        }
//    }

    public Call getMessageIA(CryptedRequestIA requestDTO){

        Gson gson = new Gson();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(500, TimeUnit.SECONDS)
                .readTimeout(500, TimeUnit.SECONDS)
                .writeTimeout(500, TimeUnit.SECONDS)
                .build();

        String json = gson.toJson(requestDTO);

//        saveinFile(json);

        RequestBody body = RequestBody.create(json, JSON);

        Request request = new Request.Builder()
                .url("http://" + baseUrl + ":8080/resposta/exigir")
                .post(body)
                .build();


        return client.newCall(request);

    }

}
