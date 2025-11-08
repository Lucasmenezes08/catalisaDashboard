package com.cesarschool.catalisabackend.models.v1Antiga.pesquisaAntiga;

import com.cesarschool.catalisabackend.models.v1Antiga.pesquisaAntiga.consumoAntigo.ConsumoAntigo;
import com.cesarschool.catalisabackend.models.v1Antiga.pesquisaAntiga.pergunta.Pergunta;
import com.cesarschool.catalisabackend.models.v1Antiga.pesquisaAntiga.resposta.Resposta;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Entity
@Getter @Setter
@Table(name="pesquisas")
public class PesquisaAntiga {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "consumo_id", nullable = false, unique = true) // garante 1:1 no BD
    private ConsumoAntigo consumoAntigo;

    @NotNull
    @OneToMany
    private List<Pergunta> perguntas;

    @NotNull
    @OneToMany(mappedBy = "pesquisa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resposta> respostas;

    public PesquisaAntiga() {}
    public PesquisaAntiga(List<Pergunta> perguntas, List<Resposta> respostas) {
        this.perguntas = perguntas;
        this.respostas = respostas;
    }
}
