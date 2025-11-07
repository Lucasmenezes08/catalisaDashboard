package com.cesarschool.catalisabackend.models.pesquisa;

import com.cesarschool.catalisabackend.models.consumo.Consumo;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Entity
@Getter @Setter
@Table(name = "pesquisas")
public class Pesquisa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "consumo_id", nullable = false)
    @NotNull
    private Consumo consumo;

    @NotNull
    @Min(0) @Max(10)
    @Setter(AccessLevel.NONE)
    private int nota;

    private String resposta;

    @NotNull
    private LocalDate dataPesquisa;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Setter(AccessLevel.NONE)
    private TipoPesquisa tipoPesquisa;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Setter(AccessLevel.NONE)
    private TipoCliente tipoCliente;

    public Pesquisa() {}
    public Pesquisa(Consumo consumo, int nota, LocalDate dataPesquisa, TipoPesquisa tipoPesquisa) {
        this.consumo = consumo;
        this.nota = nota;
        this.dataPesquisa = dataPesquisa;
        this.tipoPesquisa = tipoPesquisa;
        this.tipoCliente = definirTipo(nota, tipoPesquisa);
    }
    public Pesquisa(Consumo consumo, int nota, LocalDate dataPesquisa, TipoPesquisa tipoPesquisa, String resposta) {
        this(consumo, nota, dataPesquisa, tipoPesquisa);
        this.resposta = resposta;
    }
    public void recalcularNotaPesquisa(int nota, TipoPesquisa tipoPesquisa) {
        this.nota = nota;
        this.tipoPesquisa = tipoPesquisa;
        this.tipoCliente = definirTipo(this.nota, this.tipoPesquisa);
    }
    @PrePersist @PreUpdate
    private void garantirConsistencia() {
        this.tipoCliente = definirTipo(this.nota, this.tipoPesquisa);
    }
    private TipoCliente definirTipo(int nota, TipoPesquisa tipoPesquisa) {
        TipoCliente resultado = null;
        if (tipoPesquisa == TipoPesquisa.CSAT && (nota >= 0 && nota <= 5)) {
            if(nota >= 4){
                resultado = TipoCliente.PROMOTOR;
            }
            else if(nota == 3){
                resultado = TipoCliente.NEUTRO;
            }
            else{
                resultado = TipoCliente.DETRATOR;
            }
        }
        else if (tipoPesquisa == TipoPesquisa.NPS && (nota >= 0 && nota <= 10)) {
            if(nota >= 9){
                resultado = TipoCliente.PROMOTOR;
            }
            else if(nota >= 7){
                resultado = TipoCliente.NEUTRO;
            }
            else{
                resultado = TipoCliente.DETRATOR;
            }
        }
        else{
            throw new IllegalArgumentException("Nota inválida para o tipo de pesquisa.");
        }
        return resultado;
    }
}
