package com.example.apoiodigital.Dto;

import java.util.List;

public class ListRequisicaoRequestDTO {
    private String criacao;

    private List<RequisicaoDTO> requisicoes;

    public String getCriacao() {
        return criacao;
    }

    public void setCriacao(String criacao) {
        this.criacao = criacao;
    }

    public List<RequisicaoDTO> getRequisicoes() {
        return requisicoes;
    }

    public void setRequisicoes(List<RequisicaoDTO> requisicoes) {
        this.requisicoes = requisicoes;
    }

    public ListRequisicaoRequestDTO(String criacao, List<RequisicaoDTO> requisicoes) {
        this.criacao = criacao;
        this.requisicoes = requisicoes;
    }
}
