package com.example.apoiodigital.Utils;

public class TextUtils {
    public static String formatarTitulo(String chave) {
        String texto = chave.replaceAll("([a-z])([A-Z])", "$1 $2");
        return texto.substring(0,1).toUpperCase() + texto.substring(1).toLowerCase();
    }
}
