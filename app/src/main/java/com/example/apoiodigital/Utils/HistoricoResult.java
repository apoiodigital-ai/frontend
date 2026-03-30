package com.example.apoiodigital.Utils;

import com.example.apoiodigital.Dto.ListRequisicaoRequestDTO;

public class HistoricoResult {
    public enum Status { SUCCESS, EMPTY, NETWORK_ERROR, API_ERROR }
    public final Status status;
    private ListRequisicaoRequestDTO fullData;

    public HistoricoResult(Status status, ListRequisicaoRequestDTO fullData) {
        this.status = status;
        this.fullData = fullData;
    }

    public static HistoricoResult success(ListRequisicaoRequestDTO data) {
        if (data == null || data.getRequisicoes().isEmpty()) {
            return new HistoricoResult(Status.EMPTY, data);
        } else {
            return new HistoricoResult(Status.SUCCESS, data);
        }
    }

    public static HistoricoResult networkError() {
        return new HistoricoResult(Status.NETWORK_ERROR, null);
    }

    public static HistoricoResult apiError() {
        return new HistoricoResult(Status.API_ERROR, null);
    }

    public ListRequisicaoRequestDTO getFullData() {
        return fullData;
    }
}
