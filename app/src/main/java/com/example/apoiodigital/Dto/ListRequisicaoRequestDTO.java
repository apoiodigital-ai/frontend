package com.example.apoiodigital.Dto;

import java.util.List;

public class ListRequisicaoRequestDTO {
    private String timestamp;

    private List<RequisicaoDTO> requisicoes;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public List<RequisicaoDTO> getRequisicoes() {
        return requisicoes;
    }

    public void setRequisicoes(List<RequisicaoDTO> requisicoes) {
        this.requisicoes = requisicoes;
    }

    public ListRequisicaoRequestDTO(String timestamp, List<RequisicaoDTO> requisicoes) {
        this.timestamp = timestamp;
        this.requisicoes = requisicoes;
    }
}
