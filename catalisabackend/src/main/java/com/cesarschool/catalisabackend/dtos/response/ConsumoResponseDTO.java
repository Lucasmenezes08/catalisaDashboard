package com.cesarschool.catalisabackend.dtos.response;

import com.cesarschool.catalisabackend.models.Consumo;
import com.cesarschool.catalisabackend.models.Pesquisa;
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