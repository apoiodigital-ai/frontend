package com.example.apoiodigital.Repository;

import com.example.apoiodigital.Dto.AccessTokenDTO;
import com.example.apoiodigital.Dto.RefreshRequestDTO;
import com.example.apoiodigital.Service.ApiService;
import com.example.apoiodigital.Utils.SessionManager;

import retrofit2.Callback;

public class AuthRepository {

    private final ApiService apiService;
    private final SessionManager sessionManager;

    public AuthRepository(SessionManager sessionManager, ApiService apiService) {
        this.apiService = apiService;
        this.sessionManager = sessionManager;
    }

    public void refreshToken(Callback<AccessTokenDTO> callback) {
        String refreshToken = sessionManager.getRefreshToken();
        apiService.refresh(new RefreshRequestDTO(refreshToken)).enqueue(callback);
    }

    public boolean hasValidAccessToken() {
        return sessionManager.getAccessToken() != null;
    }
}

