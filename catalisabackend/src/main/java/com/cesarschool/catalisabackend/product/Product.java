package com.cesarschool.catalisabackend.product;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotBlank
    @Column(unique = true, nullable = false, length = 60)
    private String name;

    @NotBlank
    @Column(nullable = false, length = 30)
    private String type;

    @Column(length = 200)
    private String description;

    // Add later list field of consume

    public Product() { super(); }
    public Product(String name, String type) {
        this();
        this.name = name;
        this.type = type;
    }
    public Product(Long id, String name, String type, String description) {
        this(name, type);
        this.id = id;
        this.description = description;
    }
}