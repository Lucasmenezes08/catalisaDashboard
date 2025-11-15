package com.cesarschool.catalisabackend.models.pesquisa;

import com.cesarschool.catalisabackend.models.consumo.Consumo;

import java.time.LocalDate;

//public record PesquisaResponseDTO (
//        long id,
//        Consumo consumo,
//        int nota,
//        String resposta,
//        LocalDate dataPesquisa,
//        TipoPesquisa tipoPesquisa,
//        TipoCliente tipoCliente
//){
//    public static  PesquisaResponseDTO fromEntity(Pesquisa pesquisa){
//        return new PesquisaResponseDTO(
//                pesquisa.getId(),
//                pesquisa.getConsumo(),
//                pesquisa.getNota(),
//                pesquisa.getResposta(),
//                pesquisa.getDataPesquisa(),
//                pesquisa.getTipoPesquisa(),
//                pesquisa.getTipoCliente()
//        );
//    }
//}
public record PesquisaResponseDTO(
        long id,
        long consumoId,
        Long userId,
        Long productId,
        int nota,
        String resposta,
        LocalDate dataPesquisa,
        TipoPesquisa tipoPesquisa,
        TipoCliente tipoCliente
) {
    public static PesquisaResponseDTO fromEntity(Pesquisa p) {
        var c = p.getConsumo();
        Long uid = (c.getUser() != null) ? c.getUser().getId() : null;
        Long pid = (c.getProduto() != null) ? c.getProduto().getId() : null;

        return new PesquisaResponseDTO(
                p.getId(),
                c.getId(),
                uid,
                pid,
                p.getNota(),
                p.getResposta(),
                p.getDataPesquisa(),
                p.getTipoPesquisa(),
                p.getTipoCliente()
        );
    }
}
