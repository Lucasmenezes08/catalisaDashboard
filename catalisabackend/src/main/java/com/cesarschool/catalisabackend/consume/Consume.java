package com.cesarschool.catalisabackend.consume;

import com.cesarschool.catalisabackend.product.Product;
import com.cesarschool.catalisabackend.user.UserApp;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "consumers")
public class Consume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserApp userApp;
}
