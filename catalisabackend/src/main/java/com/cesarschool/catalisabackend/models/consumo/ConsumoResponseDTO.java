package com.cesarschool.catalisabackend.models.consumo;

import com.cesarschool.catalisabackend.models.pesquisa.Pesquisa;
import com.cesarschool.catalisabackend.models.pesquisa.PesquisaResponseDTO;
import com.cesarschool.catalisabackend.models.product.Product;
import com.cesarschool.catalisabackend.models.product.ProductResponseDTO;
import com.cesarschool.catalisabackend.models.user.User;
import com.cesarschool.catalisabackend.models.user.UserResponseDTO;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ConsumoResponseDTO (
        long id,
        UserResponseDTO user,
        ProductResponseDTO product,
        LocalDate dataConsumo,
        boolean consumiuPesquisa,
        PesquisaResponseDTO pesquisa
){
    public static ConsumoResponseDTO fromEntity(@NotNull Consumo consumo) {
        PesquisaResponseDTO pesquisaDTO = null;

        Pesquisa p = consumo.getPesquisa();
        if (p != null) {
            pesquisaDTO = PesquisaResponseDTO.fromEntity(p);
        }

        return new ConsumoResponseDTO(
                consumo.getId(),
                UserResponseDTO.fromEntity(consumo.getUser()),
                ProductResponseDTO.fromEntity(consumo.getProduto()),
                consumo.getDataConsumo(),
                consumo.isConsumiuPesquisa(),
                pesquisaDTO
        );
    }
}