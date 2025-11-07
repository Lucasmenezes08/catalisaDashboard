package com.cesarschool.catalisabackend.models.v1Antiga.pesquisaAntiga.resposta;

import com.cesarschool.catalisabackend.models.v1Antiga.pesquisaAntiga.pergunta.Pergunta;
import com.cesarschool.catalisabackend.models.v1Antiga.pesquisaAntiga.PesquisaAntiga;

public record RespostaResponseDTO(
    long id,
    PesquisaAntiga pesquisaAntiga,
    Pergunta pergunta,
    String resposta
){
    public static  RespostaResponseDTO create(Resposta resposta){
        return new RespostaResponseDTO(
                resposta.getId(),
                resposta.getPesquisaAntiga(),
                resposta.getPergunta(),
                resposta.getResposta()
        );
    }
}
