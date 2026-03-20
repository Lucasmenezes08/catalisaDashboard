package com.cesarschool.catalisabackend.dtos.request;

import com.cesarschool.catalisabackend.models.ProductType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductRequestDTO(
        @NotBlank String name,
        @NotNull ProductType type,
        String description
) {}
