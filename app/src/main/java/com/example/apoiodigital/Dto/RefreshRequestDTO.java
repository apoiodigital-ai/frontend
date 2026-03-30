package com.example.apoiodigital.Dto;

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
