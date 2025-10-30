package com.cesarschool.catalisabackend.models.pergunta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PerguntaRequestDTO(
        @NotNull @NotBlank String texto,
        @NotNull PerguntaTipo tipo,
        int notaMinima,
        int notaMaxima
) {}
