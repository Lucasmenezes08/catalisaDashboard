package com.cesarschool.catalisabackend.models.v1Antiga.pesquisaAntiga.pergunta;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum PerguntaTipo {
    TEXTO(1, "Texto"),
    MULTIPLA_ESCOLHA(2, "Multipla escolha"),
    ESCALA(3, "Escala");

    private int codigo;
    private String descricao;
    private PerguntaTipo(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }
}
