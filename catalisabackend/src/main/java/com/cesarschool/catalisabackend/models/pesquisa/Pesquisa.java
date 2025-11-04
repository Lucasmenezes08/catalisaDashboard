package com.cesarschool.catalisabackend.models.pesquisa;

import com.cesarschool.catalisabackend.models.consumo.Consumo;
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

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "consumo_id", nullable = false, unique = true) // garante 1:1 no BD
    private Consumo consumo;

    @NotNull
    @OneToMany
    private List<Pergunta> perguntas;

    @NotNull
    @OneToMany(mappedBy = "pesquisa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resposta> respostas;

    public Pesquisa() {}
    public Pesquisa(List<Pergunta> perguntas,List<Resposta> respostas) {
        this.perguntas = perguntas;
        this.respostas = respostas;
    }
}
