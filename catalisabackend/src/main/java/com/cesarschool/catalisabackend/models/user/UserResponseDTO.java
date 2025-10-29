package com.cesarschool.catalisabackend.models.user;

public record UserResponseDTO(
        Long id,
        String email,
        String username
) {}
