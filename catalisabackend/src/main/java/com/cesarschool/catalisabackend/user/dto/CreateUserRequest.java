package com.cesarschool.catalisabackend.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotBlank @Size(max = 50) String name,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8, max = 100) String password
) {}