package com.example.apoiodigital.Dto;

import java.util.UUID;

public class RequisicaoDTO {

    private UUID id;
    private String prompt;

    private String timeStamp;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public RequisicaoDTO(UUID id, String prompt, String timeStamp) {
        this.id = id;
        this.prompt = prompt;
        this.timeStamp = timeStamp;
    }
}
