package com.example.apoiodigital.feature.tutorial.data;

public class BaseDataToIADTO {


    private String prompt;
    private String contexto;
    private String id_requisicao;
    private String pergunta_especificacao;
    private String resposta_especificacao;

    public void setAdditionalInfo(AdditionalInfoDTO dto){
        this.pergunta_especificacao = dto.getPergunta();
        this.resposta_especificacao = dto.getResposta();
    }

    public BaseDataToIADTO(String prompt, String contexto, String id_requisicao) {
        this.prompt = prompt;
        this.contexto = contexto;
        this.id_requisicao = id_requisicao;
    }

    public String getContexto() {
        return contexto;
    }

    public void setContexto(String contexto) {
        this.contexto = contexto;
    }

    public String getId_requisicao() {
        return id_requisicao;
    }

    public void setId_requisicao(String id_requisicao) {
        this.id_requisicao = id_requisicao;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getPergunta_especificacao() {
        return pergunta_especificacao;
    }

    public void setPergunta_especificacao(String pergunta_especificacao) {
        this.pergunta_especificacao = pergunta_especificacao;
    }

    public String getResposta_especificacao() {
        return resposta_especificacao;
    }


}
