package com.example.apoiodigital.feature.auth.data;

public class RefreshRequestDTO {
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public RefreshRequestDTO(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
