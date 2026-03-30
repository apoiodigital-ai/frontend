package com.example.apoiodigital.Model;

import java.util.List;

public class DescryptedRequestIA {

    private String pergunta;

    private String contexto;

    private List<UiComponent> elementos;

    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }

    public void setElementos(List<UiComponent> elementos) {
        this.elementos = elementos;
    }

    public void setContexto(String contexto) {
        this.contexto = contexto;
    }

    public String getContexto() {
        return contexto;
    }

    public String getPergunta() {
        return pergunta;
    }

    public List<UiComponent> getElementos() {
        return elementos;
    }
}
