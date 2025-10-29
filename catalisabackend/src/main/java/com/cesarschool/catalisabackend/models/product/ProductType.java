package com.cesarschool.catalisabackend.models.product;

public enum ProductType {
    CURSO(1, "Curso"),
    VIDEO_AULA(2,"Video-aula"),
    ARTIGO(3, "Artigo");

    private int codigo;
    private String nome;

    private ProductType(int codigo, String nome){
        this.codigo = codigo;
        this.nome = nome;
    }

    public int getCodigo(){
        return this.codigo;
    }
    public String getNome(){
        return this.nome;
    }
}
