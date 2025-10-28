package com.cesarschool.catalisabackend.consumo;

import com.cesarschool.catalisabackend.pesquisa.Pesquisa;
import com.cesarschool.catalisabackend.product.Product;
import com.cesarschool.catalisabackend.user.UserApp;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import java.io.Serializable;

@Entity
@Setter @Getter
public class Consumo implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UserApp usuario;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Product produto;

    @NotNull
    private LocalDate dataConsumo;

    private boolean pesquisaRespondida;

    @OneToOne(mappedBy = "consumo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Pesquisa pesquisa;

    public Consumo(){}
    public Consumo(long id, UserApp usuario, Product produto, LocalDate dataConsumo, boolean pesquisaRespondida, Pesquisa pesquisa){
        this.id =id;
        this.usuario = usuario;
        this.produto = produto;
        this.dataConsumo = dataConsumo;
        this.pesquisaRespondida = pesquisaRespondida;
        this.pesquisa = pesquisa;
    }
}
