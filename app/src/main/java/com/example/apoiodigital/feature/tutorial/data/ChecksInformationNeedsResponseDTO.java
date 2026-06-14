package com.example.apoiodigital.feature.tutorial.data;

import java.util.List;

public class ChecksInformationNeedsResponseDTO {

    String contexto;

    String pergunta;
    List<String> opcoes;
    TiposPendencia tipo_pendencia;
    String descricao_duvida;

    public String getContexto() {
        return contexto;
    }


    public String getPergunta() {
        return pergunta;
    }

    public List<String> getOpcoes() {
        return opcoes;
    }

    public TiposPendencia getTipo_pendencia() {
        return tipo_pendencia;
    }

    public String getDescricao_duvida() {
        return descricao_duvida;
    }
}
