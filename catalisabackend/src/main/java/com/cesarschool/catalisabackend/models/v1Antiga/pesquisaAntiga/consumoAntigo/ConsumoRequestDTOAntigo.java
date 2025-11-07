package com.cesarschool.catalisabackend.models.v1Antiga.pesquisaAntiga.consumoAntigo;

import java.time.LocalDate;

public record ConsumoRequestDTOAntigo(
        Long userId,
        Long productId,
        LocalDate dataConsumo,
        Integer avaliacao,
        Boolean pesquisaRespondida,
        Long pesquisaId
) {}
