package com.cesarschool.catalisabackend.models.resposta;

import com.cesarschool.catalisabackend.models.pesquisa.Pesquisa;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import com.cesarschool.catalisabackend.models.pergunta.Pergunta;
import com.cesarschool.catalisabackend.models.pesquisa.Pesquisa;

import java.io.Serializable;

@Entity
@Table(name = "respostas")
@Getter @Setter
public class Resposta implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pesquisa_id", nullable = false)
    private Pesquisa pesquisa;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pergunta_id", nullable = false)
    private Pergunta pergunta;

    @Column(nullable = false)
    private String resposta;

    public Resposta() {}
    public Resposta(Pesquisa pesquisa, Pergunta pergunta, String resposta){
        this.pesquisa = pesquisa;
        this.pergunta = pergunta;
        this.resposta = resposta;
    }
}
