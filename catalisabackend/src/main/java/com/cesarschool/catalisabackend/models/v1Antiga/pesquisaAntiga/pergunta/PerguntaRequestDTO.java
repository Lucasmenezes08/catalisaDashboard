package com.cesarschool.catalisabackend.models.v1Antiga.pesquisaAntiga.pergunta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PerguntaRequestDTO(
        @NotNull @NotBlank String texto,
        @NotNull PerguntaTipo tipo,
        int notaMinima,
        int notaMaxima
) {}
