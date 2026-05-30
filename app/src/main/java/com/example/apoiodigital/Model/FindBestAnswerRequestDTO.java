package com.example.apoiodigital.Model;

import java.util.List;

public class FindBestAnswerRequestDTO {

        String contexto;
        String prompt;
        String pergunta_especificacao;
        String resposta_especificacao;

        List<UiComponent> elementos;

        public String getContexto() {
                return contexto;
        }

        public void setContexto(String contexto) {
                this.contexto = contexto;
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

        public void setResposta_especificacao(String resposta_especificacao) {
                this.resposta_especificacao = resposta_especificacao;
        }

        public List<UiComponent> getElementos() {
                return elementos;
        }

        public void setElementos(List<UiComponent> elementos) {
                this.elementos = elementos;
        }
}
