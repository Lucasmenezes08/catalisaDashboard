package com.cesarschool.catalisabackend.respostas;
import  com.cesarschool.catalisabackend.perguntas.Pergunta;
import  com.cesarschool.catalisabackend.pesquisa.Pesquisa;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import java.io.Serializable;

@Entity
@Setter @Getter

public class Resposta implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    @NotBlank
    private Pesquisa pesquisa;

    @NotBlank
    private Pergunta pergunta;

    @NotBlank
    @Column(nullable = false, length = 300)
    private String valor;

    public Resposta(){}
    public Resposta(long id, Pesquisa pesquisa, Pergunta pergunta){
        this.id = id;
        this.pergunta = pergunta;
        this.pesquisa = pesquisa;
    }
    public Resposta(long id,Pergunta pergunta, Pesquisa pesquisa, String valor){
        this(id, pesquisa, pergunta);
        this.valor = valor;
    }
}
