package com.cesarschool.catalisabackend.dtos.request;

import com.cesarschool.catalisabackend.models.Pesquisa;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ConsumoRequestDTO(
        @NotNull Long userId,
        @NotNull Long productId,
        @NotNull LocalDate dataConsumo,
        @NotNull boolean consumiuPesquisa,
        Pesquisa pesquisa
        ) {}
