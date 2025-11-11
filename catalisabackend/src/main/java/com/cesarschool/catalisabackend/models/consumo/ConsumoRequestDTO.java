package com.cesarschool.catalisabackend.models.consumo;

import com.cesarschool.catalisabackend.models.pesquisa.Pesquisa;
import com.cesarschool.catalisabackend.models.product.Product;
import com.cesarschool.catalisabackend.models.user.User;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ConsumoRequestDTO(
        @NotNull User user,
        @NotNull Product product,
        @NotNull LocalDate dataConsumo,
        @NotNull boolean consumiuPesquisa,
        @NotNull Pesquisa pesquisa
        ) {}
