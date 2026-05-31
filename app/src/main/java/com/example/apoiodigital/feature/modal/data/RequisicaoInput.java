package com.example.apoiodigital.feature.modal.data;

public class RequisicaoInput {

    private String prompt;
    private String id_usuario;

    public String getPrompt() {
        return prompt;
    }

    public RequisicaoInput(String prompt, String usuarioID) {
        this.prompt = prompt;
        this.id_usuario = usuarioID;
    }
}
