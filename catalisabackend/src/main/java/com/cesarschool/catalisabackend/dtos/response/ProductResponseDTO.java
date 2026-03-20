package com.cesarschool.catalisabackend.dtos.response;

import com.cesarschool.catalisabackend.models.Product;
import com.cesarschool.catalisabackend.models.ProductType;

public record ProductResponseDTO(
        long id,
        String name,
        ProductType type,
        String description
) {
    public static ProductResponseDTO fromEntity(Product p) {
        return new ProductResponseDTO(
                p.getId(),
                p.getName(),
                p.getType(),
                p.getDescription()
        );
    }
}
