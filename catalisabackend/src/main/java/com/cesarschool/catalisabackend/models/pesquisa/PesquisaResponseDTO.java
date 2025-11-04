package com.cesarschool.catalisabackend.models.pesquisa;

import com.cesarschool.catalisabackend.models.consumo.Consumo;
import com.cesarschool.catalisabackend.models.pergunta.Pergunta;
import com.cesarschool.catalisabackend.models.resposta.Resposta;

import java.util.List;

public record PesquisaResponseDTO(
        long id,
        Consumo consumo,
        List<Pergunta> perguntas,
        List<Resposta> respostas
) {
    public static PesquisaResponseDTO fromEntity(Pesquisa pesquisa) {
        return new PesquisaResponseDTO(
                pesquisa.getId(),
                pesquisa.getConsumo(),
                pesquisa.getPerguntas(),
                pesquisa.getRespostas()
        );
    }
}
