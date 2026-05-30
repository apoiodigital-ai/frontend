package com.example.apoiodigital.Dto;

public class FindBestAnswerResponseDTO {

    Integer viewID;
    String novo_contexto;
    String mensagem_escrita;
    String mensagem_voz;

    public Integer getViewID() {
        return viewID;
    }

    public void setViewID(Integer viewID) {
        this.viewID = viewID;
    }

    public String getNovo_contexto() {
        return novo_contexto;
    }

    public void setNovo_contexto(String novo_contexto) {
        this.novo_contexto = novo_contexto;
    }

    public String getMensagem_escrita() {
        return mensagem_escrita;
    }

    public void setMensagem_escrita(String mensagem_escrita) {
        this.mensagem_escrita = mensagem_escrita;
    }

    public String getMensagem_voz() {
        return mensagem_voz;
    }

    public void setMensagem_voz(String mensagem_voz) {
        this.mensagem_voz = mensagem_voz;
    }
}
