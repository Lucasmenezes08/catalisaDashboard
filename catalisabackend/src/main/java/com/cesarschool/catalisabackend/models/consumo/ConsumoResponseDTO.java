package com.cesarschool.catalisabackend.models.consumo;

import com.cesarschool.catalisabackend.models.pesquisa.Pesquisa;
import com.cesarschool.catalisabackend.models.product.Product;
import com.cesarschool.catalisabackend.models.user.User;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ConsumoResponseDTO (
        long id,
        User user,
        Product product,
        LocalDate dataConsumo,
        boolean consumiuPesquisa,
        Pesquisa pesquisa
){
    public static ConsumoResponseDTO fromEntity (@NotNull Consumo consumo){
        return new ConsumoResponseDTO(
                consumo.getId(),
                consumo.getUser(),
                consumo.getProduto(),
                consumo.getDataConsumo(),
                consumo.isConsumiuPesquisa(),
                consumo.getPesquisa()
        );
    }
}
