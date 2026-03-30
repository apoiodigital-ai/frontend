package com.example.apoiodigital.ViewModel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.apoiodigital.Dto.IAResponseDTO;
import com.example.apoiodigital.Repository.HistoryRepository;
import com.example.apoiodigital.Utils.HistoricoResult;

import java.util.List;

public class HistoryViewModel extends ViewModel {

    private HistoryRepository repository;

    public HistoryViewModel(HistoryRepository repository) {
        this.repository = repository;
    }

    public LiveData<HistoricoResult> getHistorico() {
        return repository.getHistorico();
    }

    public LiveData<List<IAResponseDTO>> getIAResposeByRequestId(String idReq){
        return repository.getIAResponse(idReq);
    }
}
