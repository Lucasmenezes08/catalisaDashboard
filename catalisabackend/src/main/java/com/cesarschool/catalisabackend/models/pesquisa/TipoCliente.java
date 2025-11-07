package com.cesarschool.catalisabackend.models.pesquisa;

import lombok.Getter;

@Getter
public enum TipoCliente {
    PROMOTOR(1, "Promotor"),
    NEUTRO(2, "Neutro"),
    DETRATOR(3, "Detrator");

    private final int codigo;
    private final String descricao;
    private TipoCliente(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }
}
