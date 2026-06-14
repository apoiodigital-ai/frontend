package com.example.apoiodigital.feature.screen_question;

public class UserAnswerValidatorResponseDTO {

    String pergunta;
    boolean satisfaz;

    String pergunta_especificacao;
    String resposta_especificacao;

    public String getPergunta_especificacao() {
        return pergunta_especificacao;
    }

    public String getResposta_especificacao() {
        return resposta_especificacao;
    }

    public String getPergunta() {
        return pergunta;
    }

    public boolean isSatisfaz() {
        return satisfaz;
    }

}
