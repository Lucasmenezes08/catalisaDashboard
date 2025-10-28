package com.cesarschool.catalisabackend.perguntas;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import java.io.Serializable;

@Entity
@Setter @Getter
public class Pergunta implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    @NotBlank
    @Column(unique = true, nullable = false, length = 600)
    private String texto;


    private int notaMinima;
    private int notaMaxima;

    @NotBlank
    @Column(nullable = false, length = 30)
    private String tipo;

    public Pergunta(){}
    public Pergunta(long id, String texto, String tipo){
        this.id = id;
        this.texto = texto;
        this.tipo = tipo;
    }
    public Pergunta(long id, String texto, String tipo, int notaMinima, int notaMaxima){
        this(id, texto, tipo);
        this.notaMaxima = notaMaxima;
        this.notaMinima = notaMinima;
    }
}
