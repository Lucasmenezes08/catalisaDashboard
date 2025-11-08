package com.cesarschool.catalisabackend.models.v1Antiga.pesquisaAntiga.resposta;

import com.cesarschool.catalisabackend.models.v1Antiga.pesquisaAntiga.PesquisaAntiga;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import com.cesarschool.catalisabackend.models.v1Antiga.pesquisaAntiga.pergunta.Pergunta;

import java.io.Serializable;

@Entity
@Table(
        name = "respostas",
        uniqueConstraints = @UniqueConstraint(columnNames = {"pesquisa_id", "pergunta_id"})
)
@Getter @Setter
public class Resposta implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pesquisa_id", nullable = false)
    private PesquisaAntiga pesquisaAntiga;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pergunta_id", nullable = false)
    private Pergunta pergunta;

    @Column(nullable = false)
    private String resposta;

    public Resposta() {}
    public Resposta(PesquisaAntiga pesquisaAntiga, Pergunta pergunta, String resposta){
        this.pesquisaAntiga = pesquisaAntiga;
        this.pergunta = pergunta;
        this.resposta = resposta;
    }
}
