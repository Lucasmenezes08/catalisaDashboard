package com.cesarschool.catalisabackend.pesquisa;
import jakarta.validation.constraints.NotNull;
import  com.cesarschool.catalisabackend.consumo.Consumo;
import  com.cesarschool.catalisabackend.perguntas.Pergunta;
import  com.cesarschool.catalisabackend.respostas.Resposta;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import java.io.Serializable;

@Entity
@Setter @Getter
public class Pesquisa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consumo_id", nullable = false, unique = true)
    @NotNull
    private Consumo consumo;

    @OneToMany(mappedBy = "pesquisa", cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    private Pergunta[] perguntas;

    @OneToMany(mappedBy = "pesquisa", cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    private Resposta[] respostas;

    public Pesquisa(){}
    public Pesquisa(long id, Consumo consumo, Pergunta[] perguntas, Resposta[] respostas){
        this.id = id;
        this.consumo = consumo;
        this.perguntas = perguntas;
        this.respostas = respostas;
    }
}
