package com.example.apoiodigital.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apoiodigital.Model.CryptedResponseIA;
import com.example.apoiodigital.Model.DescryptedResponseIA;
import com.example.apoiodigital.Service.CryptService;
import com.google.gson.Gson;

public class TutorialActivity extends AppCompatActivity {

    private CryptService cryptService = new CryptService();
    private DescryptedResponseIA descryptedResponseIA;


    private final BroadcastReceiver receiverIdentifidor = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Gson gson = new Gson();
            String response = intent.getStringExtra("responseIA");

            CryptedResponseIA responseModel = gson.fromJson(response, CryptedResponseIA.class);

            var key = responseModel.getKey();
            var cryptedText = responseModel.getIaMessage();
            var descryptedJson = cryptService.descrypt(cryptedText, key);
            descryptedResponseIA = gson.fromJson(descryptedJson, DescryptedResponseIA.class);

//          mask.setPositions(responseModel.getIdentifidor().getBounds());
        }
    };

    public DescryptedResponseIA getDescryptedResponseIA(){
        return descryptedResponseIA;
    }

}
