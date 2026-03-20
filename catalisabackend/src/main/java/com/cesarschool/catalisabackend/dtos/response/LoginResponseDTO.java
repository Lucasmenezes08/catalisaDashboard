package com.cesarschool.catalisabackend.dtos.response;

public record LoginResponseDTO(
        boolean authenticated,
        String message
) {}
