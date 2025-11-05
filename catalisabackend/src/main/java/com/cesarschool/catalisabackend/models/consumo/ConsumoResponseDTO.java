package com.cesarschool.catalisabackend.models.consumo;
import com.cesarschool.catalisabackend.models.pesquisa.Pesquisa;
import com.cesarschool.catalisabackend.models.product.Product;
import com.cesarschool.catalisabackend.models.user.User;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ConsumoResponseDTO(
        long id,
        Long userId,
        Long productId,
        LocalDate dataConsumo,
        int avaliacao,
        boolean pesquisaRespondida,
        Long pesquisaId
) {}
