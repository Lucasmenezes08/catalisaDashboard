package com.cesarschool.catalisabackend.models.user;

public record UserRequestDTO(
        String email,
        String cpfCnpj,
        String username,
        String password
) {}
