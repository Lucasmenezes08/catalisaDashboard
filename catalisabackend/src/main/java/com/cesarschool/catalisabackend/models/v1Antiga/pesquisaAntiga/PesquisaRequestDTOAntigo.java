package com.cesarschool.catalisabackend.models.v1Antiga.pesquisaAntiga;

import com.cesarschool.catalisabackend.models.v1Antiga.pesquisaAntiga.consumoAntigo.ConsumoAntigo;
import com.cesarschool.catalisabackend.models.v1Antiga.pesquisaAntiga.pergunta.Pergunta;
import com.cesarschool.catalisabackend.models.v1Antiga.pesquisaAntiga.resposta.Resposta;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PesquisaRequestDTOAntigo(
        @NotNull List<Pergunta> perguntas,
        @NotNull List<Resposta> respostas,
        @NotNull ConsumoAntigo consumoAntigo
        ) {
}
