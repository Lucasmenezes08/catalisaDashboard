package com.cesarschool.catalisabackend.models.pergunta;

import com.cesarschool.catalisabackend.models.product.Product;
import com.cesarschool.catalisabackend.models.product.ProductResponseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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