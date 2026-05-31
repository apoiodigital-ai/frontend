package com.example.apoiodigital.feature.modal.data;

public class Requisicao {

    private String id;
    private String prompt;
    private String timeStamp;

    private AppSuportado appSuportado;

    public AppSuportado getAppSuportado() {
        return appSuportado;
    }

    public void setAppSuportado(AppSuportado appSuportado) {
        this.appSuportado = appSuportado;
    }

    public String getId() {
        return id;
    }

    public String getPrompt() {
        return prompt;
    }

    public String getTimeStamp() {
        return timeStamp;
    }
}
