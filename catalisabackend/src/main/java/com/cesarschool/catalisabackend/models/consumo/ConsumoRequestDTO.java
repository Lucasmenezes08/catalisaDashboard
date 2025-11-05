package com.cesarschool.catalisabackend.models.consumo;

import com.cesarschool.catalisabackend.models.pesquisa.Pesquisa;
import com.cesarschool.catalisabackend.models.product.Product;
import com.cesarschool.catalisabackend.models.resposta.Resposta;
import com.cesarschool.catalisabackend.models.user.User;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record ConsumoRequestDTO(
        Long userId,
        Long productId,
        LocalDate dataConsumo,
        Integer avaliacao,
        Boolean pesquisaRespondida,
        Long pesquisaId
) {}
