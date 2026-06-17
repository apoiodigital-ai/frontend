package com.example.apoiodigital.feature.tutorial.data;

public class AdditionalInfoDTO {

    private String pergunta;
    private String resposta;

    public AdditionalInfoDTO(String pergunta, String resposta) {
        this.pergunta = pergunta;
        this.resposta = resposta;
    }

    public String getPergunta() {
        return pergunta;
    }

    public String getResposta() {
        return resposta;
    }

}
