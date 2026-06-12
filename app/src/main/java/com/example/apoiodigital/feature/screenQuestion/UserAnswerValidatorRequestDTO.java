package com.example.apoiodigital.feature.screenQuestion;

import com.example.apoiodigital.feature.tutorial.data.TiposPendencia;

public class UserAnswerValidatorRequestDTO {

    String contexto;
    String pergunta;
    String resposta_escrita;
    TiposPendencia tipo_dependencia;
    String descricao_duvida;

    public UserAnswerValidatorRequestDTO(String contexto, String pergunta, String resposta_escrita, TiposPendencia tipo_dependencia, String descricao_duvida) {
        this.contexto = contexto;
        this.pergunta = pergunta;
        this.resposta_escrita = resposta_escrita;
        this.tipo_dependencia = tipo_dependencia;
        this.descricao_duvida = descricao_duvida;
    }

}
