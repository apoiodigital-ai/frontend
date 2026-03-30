package com.example.apoiodigital.Model;

public class CryptedRequestIA {

    private String key;

    private String textCrypted;

    private String id_requisicao;

    public void setId_requisicao(String id_requisicao) {
        this.id_requisicao = id_requisicao;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setTextCrypted(String textCrypted) {
        this.textCrypted = textCrypted;
    }
}
