package com.example.apoiodigital.Model;

public class RequisicaoInput {

    private String pergunta;
    private String id_usuario;

    public String getPrompt() {
        return pergunta;
    }

    public RequisicaoInput(String prompt, String usuarioID) {
        this.pergunta = prompt;
        this.id_usuario = usuarioID;
    }
}
