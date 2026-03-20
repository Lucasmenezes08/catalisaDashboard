package com.cesarschool.catalisabackend.dtos.response;

import com.cesarschool.catalisabackend.models.Pesquisa;
import com.cesarschool.catalisabackend.models.TipoCliente;
import com.cesarschool.catalisabackend.models.TipoPesquisa;

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
        TipoCliente tipoCliente,
        String produto
) {
    public static PesquisaResponseDTO fromEntity(Pesquisa p) {
        var c = p.getConsumo();
        Long uid = (c.getUser() != null) ? c.getUser().getId() : null;
        Long pid = (c.getProduto() != null) ? c.getProduto().getId() : null;
        String nameProduto;
        if(p.getConsumo().getProduto().getName() == null){
            nameProduto = "Sem produto";
        }
        else{
            nameProduto = p.getConsumo().getProduto().getName();
        }
        return new PesquisaResponseDTO(
                p.getId(),
                c.getId(),
                uid,
                pid,
                p.getNota(),
                p.getResposta(),
                p.getDataPesquisa(),
                p.getTipoPesquisa(),
                p.getTipoCliente(),
                nameProduto
        );
    }
}
