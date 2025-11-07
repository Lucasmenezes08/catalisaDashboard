package com.cesarschool.catalisabackend.models.consumo;

import com.cesarschool.catalisabackend.models.pesquisa.Pesquisa;
import com.cesarschool.catalisabackend.models.product.Product;
import com.cesarschool.catalisabackend.models.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Getter @Setter
//@ToString
//@AllArgsConstructor
@Table(name = "consumos")
public class Consumo {
    private long id;
    private User user;
    private boolean consumiuPesquisa;
    private Product produto;
    private LocalDate dataConsumo;
    private Pesquisa pesquisa;
}
