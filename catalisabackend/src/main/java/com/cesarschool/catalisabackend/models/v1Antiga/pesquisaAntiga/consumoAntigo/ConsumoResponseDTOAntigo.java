package com.cesarschool.catalisabackend.models.v1Antiga.pesquisaAntiga.consumoAntigo;

import java.time.LocalDate;

public record ConsumoResponseDTOAntigo(
        long id,
        Long userId,
        Long productId,
        LocalDate dataConsumo,
        int avaliacao,
        boolean pesquisaRespondida,
        Long pesquisaId
) {}
