package com.example.apoiodigital.feature.tutorial.data;

import java.util.List;

public class ChecksInformationNeedsRequestDTO {

    List<Componente> elementos;
    String contexto;
    String prompt;

    public ChecksInformationNeedsRequestDTO(List<Componente> elementos, String contexto, String prompt) {
        this.elementos = elementos;
        this.contexto = contexto;
        this.prompt = prompt;
    }
}
