package com.example.apoiodigital.feature.modal.service;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

public class OpenAppService {

    public String openAppByPacoteAndReturnsContexto(Context context, String pacoteApp, String nomeApp){
        String contextoTela = "O usuario acabou de abrir a o aplicativo ";

        PackageManager pm = context.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(pacoteApp);

        if (intent != null) {
            // O app está instalado, vamos abri-lo
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            contextoTela += nomeApp + " e segue esperando para o proximo passo";
        } else {
            // O app NÃO está instalado, vamos para a Play Store
            try {
                // Tenta abrir diretamente pelo app da Play Store
                Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + pacoteApp));
                marketIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(marketIntent);
                contextoTela += "PlayStore. Ele precisa instalar o aplicativo " + nomeApp + " primeiro";
            } catch (ActivityNotFoundException e) {
                // Se a Play Store não estiver instalada, abre pelo navegador
                contextoTela += "Navegador no site da PlayStore. Ele precisa instalar o aplicativo " + nomeApp + " primeiro";

                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + pacoteApp));
                webIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(webIntent);
            }
        }

        return contextoTela;
    }
}
