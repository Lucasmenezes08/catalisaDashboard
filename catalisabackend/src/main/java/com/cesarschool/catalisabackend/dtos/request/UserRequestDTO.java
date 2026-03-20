package com.cesarschool.catalisabackend.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequestDTO(
        @NotBlank @Email String email,
        @NotBlank String cpfCnpj,
        String username,
        @NotBlank String password
) {}

