package com.example.apoiodigital.Dto;

public class IAResponseDTO {
    private String key;
    private String iaMessage;

    public IAResponseDTO(String key, String iaMessage) {
        this.key = key;
        this.iaMessage = iaMessage;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getiaMessage() {
        return iaMessage;
    }

    public void setiaMessage(String iaMessage) {
        this.iaMessage = iaMessage;
    }
}
