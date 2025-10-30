package com.cesarschool.catalisabackend.models.product;

import com.cesarschool.catalisabackend.models.product.Product;
import com.cesarschool.catalisabackend.models.product.ProductType;

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
