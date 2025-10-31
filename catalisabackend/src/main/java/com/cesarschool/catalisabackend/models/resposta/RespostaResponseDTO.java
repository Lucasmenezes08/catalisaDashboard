package com.cesarschool.catalisabackend.models.resposta;

import com.cesarschool.catalisabackend.models.pergunta.Pergunta;

public record RespostaResponseDTO(
    long id,
    Pesquisa pesquisa,
    Pergunta pergunta,
    String resposta
){
    public static  RespostaResponseDTO create(Resposta resposta){
        return new RespostaResponseDTO(
                resposta.getId(),
                resposta.getPesquisa(),
                resposta.getPergunta(),
                resposta.getResposta()
        );
    }
}
