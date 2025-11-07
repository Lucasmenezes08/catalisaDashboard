package com.cesarschool.catalisabackend.models.pesquisa;

import lombok.Getter;

@Getter
public enum TipoPesquisa {
    NPS(1, "NPS"),
    CSAT(2, "CSAT");

    private final int codigo;
    private final String descricao;
    TipoPesquisa(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }
}
