package com.cesarschool.catalisabackend.models.pesquisa;

import com.cesarschool.catalisabackend.models.consumo.Consumo;
import com.cesarschool.catalisabackend.models.pergunta.Pergunta;
import com.cesarschool.catalisabackend.models.resposta.Resposta;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PesquisaRequestDTO(
        @NotNull List<Pergunta> perguntas,
        @NotNull List<Resposta> respostas,
        @NotNull Consumo consumo
        ) {
}
