package com.cesarschool.catalisabackend.models.pesquisa;

import com.cesarschool.catalisabackend.models.pergunta.Pergunta;
import com.cesarschool.catalisabackend.models.resposta.Resposta;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Entity
@Getter @Setter
@Table(name="pesquisas")
public class Pesquisa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@NotNull
    //@ManyToOne(fetch = FetchType.LAZY)
    //private Consumo consumo

    @NotNull
    @OneToMany
    private List<Pergunta> perguntas;

    @NotNull
    @OneToMany
    private List<Resposta> respostas;

    public Pesquisa() {}
    public Pesquisa(List<Pergunta> perguntas,List<Resposta> respostas) {
        this.perguntas = perguntas;
        this.respostas = respostas;
    }
}
