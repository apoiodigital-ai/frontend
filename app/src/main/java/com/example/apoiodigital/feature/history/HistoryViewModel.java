package com.example.apoiodigital.feature.history;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.apoiodigital.feature.history.data.IAResponseDTO;
import com.example.apoiodigital.core.Utils.HistoricoResult;
import com.example.apoiodigital.feature.history.repository.HistoryRepository;

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
