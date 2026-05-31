package com.example.apoiodigital.core.Utils;

import com.example.apoiodigital.feature.history.data.RequisicaoDTO;

public class RequestItem {
    public enum Type { TITLE, ITEM }

    public Type type;
    public String title;
    public RequisicaoDTO requisicao;

    public static RequestItem createTitle(String title) {
        RequestItem item = new RequestItem();
        item.type = Type.TITLE;
        item.title = TextUtils.formatarTitulo(title);
        return item;
    }

    public static RequestItem createItem(RequisicaoDTO requisicao) {
        RequestItem item = new RequestItem();
        item.type = Type.ITEM;
        item.requisicao = requisicao;
        return item;
    }
}
