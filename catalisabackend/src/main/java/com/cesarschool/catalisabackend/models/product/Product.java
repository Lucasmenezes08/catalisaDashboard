package com.cesarschool.catalisabackend.models.product;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Entity
@Table(name = "products")
@Getter @Setter
public class Product implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(nullable = false)
    @NotBlank
    private String name;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductType type;

    private String description;

    public Product(){}
    public Product(String name, ProductType type){
        this.name = name;
        this.type = type;
    }
    public Product(String name, ProductType type, String description){
        this(name, type);
        this.description = description;
    }
}
