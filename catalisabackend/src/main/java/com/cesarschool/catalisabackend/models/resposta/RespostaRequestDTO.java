package com.cesarschool.catalisabackend.models.resposta;

import com.cesarschool.catalisabackend.models.pergunta.Pergunta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.cesarschool.catalisabackend.models.pesquisa.Pesquisa;
public record RespostaRequestDTO(
    @NotNull Pesquisa pesquisa,
    Pergunta pergunta,
    @NotBlank String resposta
){}
