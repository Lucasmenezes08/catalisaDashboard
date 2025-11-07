package com.cesarschool.catalisabackend.models.v1Antiga.pesquisaAntiga.resposta;

import com.cesarschool.catalisabackend.models.v1Antiga.pesquisaAntiga.pergunta.Pergunta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.cesarschool.catalisabackend.models.v1Antiga.pesquisaAntiga.PesquisaAntiga;
public record RespostaRequestDTO(
    @NotNull PesquisaAntiga pesquisaAntiga,
    Pergunta pergunta,
    @NotBlank String resposta
){}
