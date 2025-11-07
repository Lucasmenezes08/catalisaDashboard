package com.cesarschool.catalisabackend.models.v1Antiga.pesquisaAntiga.pergunta;

public record PerguntaResponseDTO(
        long id,
        String texto,
        PerguntaTipo tipo,
        int notaMinima,
        int notaMaxima
){
    public static PerguntaResponseDTO fromEntity(Pergunta p) {
        return new PerguntaResponseDTO(
                p.getId(),
                p.getTexto(),
                p.getTipo(),
                p.getNotaMinima(),
                p.getNotaMaxima()
        );
    }
}