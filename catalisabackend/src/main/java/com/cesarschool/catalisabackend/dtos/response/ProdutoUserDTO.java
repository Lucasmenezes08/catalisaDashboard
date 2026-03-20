package com.cesarschool.catalisabackend.dtos.response;

public record ProdutoUserDTO(
        Long id,
        String email,
        String username,
        String nameProduto
){}
