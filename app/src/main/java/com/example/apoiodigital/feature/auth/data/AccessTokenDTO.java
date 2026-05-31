package com.example.apoiodigital.feature.auth.data;

public class AccessTokenDTO {
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public AccessTokenDTO(String accessToken) {
        this.accessToken = accessToken;
    }
}
