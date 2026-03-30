package com.example.apoiodigital.Dto;

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
