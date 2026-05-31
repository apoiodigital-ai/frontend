package com.example.apoiodigital.feature.tutorial;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class TutorialActivity extends AppCompatActivity {


    private final BroadcastReceiver receiverIdentifidor = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            Gson gson = new Gson();
//            String response = intent.getStringExtra("responseIA");
//
//            FindBestAnswerResponseDTO responseModel = gson.fromJson(response, FindBestAnswerResponseDTO.class);
//
//            var descryptedJson = cryptService.descrypt(cryptedText, key);
//            descryptedResponseIA = gson.fromJson(descryptedJson, DescryptedResponseIA.class);

//            mask.setPositions(responseModel.getIdentifidor().getBounds());
        }
    };

}
