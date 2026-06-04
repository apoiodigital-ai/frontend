package com.example.apoiodigital.feature.modal.service;

import android.content.Context;

import com.example.apoiodigital.core.Utils.SessionManager;

public class TokenService {

    private SessionManager sessionManager;

    public String getAccessToken(Context context) {
        sessionManager = new SessionManager(context);
        String token = sessionManager.getAccessToken();
        if(token == null || token.isEmpty()) return null;

        return token;
    }

}
