package com.example.apoiodigital.Model;

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
