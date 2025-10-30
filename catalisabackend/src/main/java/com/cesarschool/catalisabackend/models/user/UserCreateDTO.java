package com.cesarschool.catalisabackend.models.user;

public record UserCreateDTO(
        String email,
        String cpfCnpj,
        String username,
        String password
) {}
