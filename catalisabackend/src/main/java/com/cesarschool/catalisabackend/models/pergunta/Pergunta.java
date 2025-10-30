package com.cesarschool.catalisabackend.models.pergunta;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter @Setter
@Table(name = "perguntas")
public class Pergunta implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Column(nullable = false)
    private String texto;

    private int notaMinima; // filtro de acordo com a nota do usuário
    private int notaMaxima;

    @NotBlank
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PerguntaTipo tipo; // texto, multipla escolha, escala, etc.

    public Pergunta() {}
    public Pergunta(String texto, PerguntaTipo tipo) {
        this.texto = texto;
        this.tipo = tipo;
    }
    public Pergunta(String texto, int notaMinima, int notaMaxima, PerguntaTipo tipo) {
        this(texto, tipo);
        this.notaMinima = notaMinima;
        this.notaMaxima = notaMaxima;
    }
}
