package com.cesarschool.catalisabackend.models.v1Antiga.pesquisaAntiga.consumoAntigo;

import com.cesarschool.catalisabackend.models.v1Antiga.pesquisaAntiga.PesquisaAntiga;
import com.cesarschool.catalisabackend.models.product.Product;
import com.cesarschool.catalisabackend.models.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "consumos")
@Getter @Setter
public class ConsumoAntigo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull
    @Column(name = "data_consumo", nullable = false)
    private LocalDate dataConsumo;

    @Min(1) @Max(5)
    @Column(nullable = false)
    private int avaliacao; // nota de 1 a 5

    @Column(nullable = false)
    private boolean pesquisaRespondida;

    @OneToOne(mappedBy = "consumo", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private PesquisaAntiga pesquisaAntiga;

    public ConsumoAntigo() {}
    public ConsumoAntigo(User user, Product product, LocalDate dataConsumo, int avaliacao, boolean pesquisaRespondida, PesquisaAntiga pesquisaAntiga) {
        this.user = user;
        this.product = product;
        this.dataConsumo = dataConsumo;
        this.avaliacao = avaliacao;
        this.pesquisaRespondida = pesquisaRespondida;
        this.pesquisaAntiga = pesquisaAntiga;
    }
}
