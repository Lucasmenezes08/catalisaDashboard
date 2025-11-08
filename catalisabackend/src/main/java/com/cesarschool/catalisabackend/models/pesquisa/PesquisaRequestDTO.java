package com.cesarschool.catalisabackend.models.pesquisa;

import com.cesarschool.catalisabackend.models.consumo.Consumo;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PesquisaRequestDTO(
        @NotNull long consumoId,
        @NotNull int nota,
        String resposta,
        @NotNull LocalDate dataPesquisa,
        @NotNull TipoPesquisa tipoPesquisa
){}
