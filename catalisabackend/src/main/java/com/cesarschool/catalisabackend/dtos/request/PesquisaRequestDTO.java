package com.cesarschool.catalisabackend.dtos.request;

import com.cesarschool.catalisabackend.models.TipoPesquisa;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PesquisaRequestDTO(
        @NotNull long consumoId,
        @NotNull int nota,
        String resposta,
        @NotNull LocalDate dataPesquisa,
        @NotNull TipoPesquisa tipoPesquisa
){}
