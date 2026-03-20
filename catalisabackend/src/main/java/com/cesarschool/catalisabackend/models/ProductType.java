package com.cesarschool.catalisabackend.models;
import lombok.Getter;

@Getter
public enum ProductType {
    CURSO(1, "Curso"),
    VIDEO_AULA(2,"Video-aula"),
    ARTIGO(3, "Artigo"),
    CURSO_ONLINE(4,"Curso-Online"),
    FINANCAS(5, "Finanças");

    private int codigo;
    private String nome;

    private ProductType(int codigo, String nome){
        this.codigo = codigo;
        this.nome = nome;
    }
}
