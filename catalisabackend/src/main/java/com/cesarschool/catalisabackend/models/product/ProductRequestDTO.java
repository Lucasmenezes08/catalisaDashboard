package com.cesarschool.catalisabackend.models.product;

import com.cesarschool.catalisabackend.models.product.ProductType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductRequestDTO(
        @NotBlank String name,
        @NotNull ProductType type,
        String description
) {}
