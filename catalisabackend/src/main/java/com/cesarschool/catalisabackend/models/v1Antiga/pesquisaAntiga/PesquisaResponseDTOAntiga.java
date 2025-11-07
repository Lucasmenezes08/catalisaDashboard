package com.cesarschool.catalisabackend.models.v1Antiga.pesquisaAntiga;

import com.cesarschool.catalisabackend.models.v1Antiga.pesquisaAntiga.consumoAntigo.ConsumoAntigo;
import com.cesarschool.catalisabackend.models.v1Antiga.pesquisaAntiga.pergunta.Pergunta;
import com.cesarschool.catalisabackend.models.v1Antiga.pesquisaAntiga.resposta.Resposta;

import java.util.List;

public record PesquisaResponseDTOAntiga(
        long id,
        ConsumoAntigo consumoAntigo,
        List<Pergunta> perguntas,
        List<Resposta> respostas
) {
    public static PesquisaResponseDTOAntiga fromEntity(PesquisaAntiga pesquisaAntiga) {
        return new PesquisaResponseDTOAntiga(
                pesquisaAntiga.getId(),
                pesquisaAntiga.getConsumoAntigo(),
                pesquisaAntiga.getPerguntas(),
                pesquisaAntiga.getRespostas()
        );
    }
}
