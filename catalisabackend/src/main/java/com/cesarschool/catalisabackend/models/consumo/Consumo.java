package com.cesarschool.catalisabackend.models.consumo;

import com.cesarschool.catalisabackend.models.pesquisa.Pesquisa;
import com.cesarschool.catalisabackend.models.product.Product;
import com.cesarschool.catalisabackend.models.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "consumos")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Consumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @EqualsAndHashCode.Include
    private long id;

    // Muitos consumos para um usuário
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private User user;

    // Muitos consumos para um produto
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    @NotNull
    private Product produto;

    @Column(nullable = false)
    private boolean consumiuPesquisa;

    @Column(name = "data_consumo", nullable = false)
    private LocalDate dataConsumo;

    // Lado inverso da relação 1-1 com Pesquisa (dono está em Pesquisa.consumo)
    @OneToOne(mappedBy = "consumo", fetch = FetchType.LAZY)
    private Pesquisa pesquisa;

    public Consumo(){}
    public Consumo(User user, Product produto, boolean consumiuPesquisa, LocalDate dataConsumo) {
        this.user = user;
        this.produto = produto;
        this.consumiuPesquisa = consumiuPesquisa;
        this.dataConsumo = dataConsumo;
    }
    public Consumo(User user, Product produto, boolean consumiuPesquisa, Pesquisa pesquisa, LocalDate dataConsumo) {
        this(user, produto,consumiuPesquisa,dataConsumo);
        this.pesquisa = pesquisa;
    }
}
