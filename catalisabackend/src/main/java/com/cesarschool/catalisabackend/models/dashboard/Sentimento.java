package com.cesarschool.catalisabackend.models.dashboard;

import lombok.Getter;

@Getter
public enum Sentimento {
    MUITO_POSITIVO(5, "Muito positivo"),
    POSITIVO(4, "Positivo"),
    NEUTRO(3, "Neutro"),
    NEGATIVO(2, "Negativo"),
    MUITO_NEGATIVO(1, "Muito negativo");

    private final int codigo;
    private final String descricao;

    Sentimento(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }
}
