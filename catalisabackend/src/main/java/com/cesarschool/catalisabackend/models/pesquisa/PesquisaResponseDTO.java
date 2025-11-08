package com.cesarschool.catalisabackend.models.pesquisa;

import com.cesarschool.catalisabackend.models.consumo.Consumo;

import java.time.LocalDate;

public record PesquisaResponseDTO (
        long id,
        Consumo consumo,
        int nota,
        String resposta,
        LocalDate dataPesquisa,
        TipoPesquisa tipoPesquisa,
        TipoCliente tipoCliente
){
    public static  PesquisaResponseDTO fromEntity(Pesquisa pesquisa){
        return new PesquisaResponseDTO(
                pesquisa.getId(),
                pesquisa.getConsumo(),
                pesquisa.getNota(),
                pesquisa.getResposta(),
                pesquisa.getDataPesquisa(),
                pesquisa.getTipoPesquisa(),
                pesquisa.getTipoCliente()
        );
    }
}
