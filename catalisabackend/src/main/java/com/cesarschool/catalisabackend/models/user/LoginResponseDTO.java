package com.cesarschool.catalisabackend.models.user;

public record LoginResponseDTO(
        boolean authenticated,
        String message
) {}
