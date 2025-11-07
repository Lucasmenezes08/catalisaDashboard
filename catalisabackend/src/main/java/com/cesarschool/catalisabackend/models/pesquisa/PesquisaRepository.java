package com.cesarschool.catalisabackend.models.pesquisa;

import com.cesarschool.catalisabackend.models.consumo.Consumo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PesquisaRepository
        extends JpaRepository<Pesquisa, Long>, JpaSpecificationExecutor<Pesquisa> {

    Optional<Pesquisa> findByConsumo(Consumo consumo);
    Page<Pesquisa> findByTipoPesquisa(TipoPesquisa tipoPesquisa, Pageable pageable);

    List<Pesquisa> findByTipoPesquisaAndTipoCliente(TipoPesquisa tipoPesquisa, TipoCliente tipoCliente);
    Page<Pesquisa> findByTipoPesquisaAndTipoCliente(TipoPesquisa tipoPesquisa, TipoCliente tipoCliente, Pageable pageable);

    List<Pesquisa> findByTipoPesquisaAndDataPesquisaBetween(TipoPesquisa tipoPesquisa, LocalDate inicio, LocalDate fim);
    Page<Pesquisa> findByTipoPesquisaAndDataPesquisaBetween(TipoPesquisa tipoPesquisa, LocalDate inicio, LocalDate fim, Pageable pageable);

    Optional<Pesquisa> findTopByConsumoOrderByDataPesquisaDesc(Consumo consumo);
    boolean existsByConsumoAndDataPesquisa(Consumo consumo, LocalDate dataPesquisa);


    long countByTipoPesquisaAndTipoClienteAndDataPesquisaBetween(
            TipoPesquisa tipoPesquisa, TipoCliente tipoCliente, LocalDate inicio, LocalDate fim
    );

    @Query("""
           select avg(p.nota) 
           from Pesquisa p
           where p.tipoPesquisa = :tipo
             and p.dataPesquisa between :inicio and :fim
           """)
    Double mediaNotaPorTipoPesquisa(@Param("tipo") TipoPesquisa tipo,
                                    @Param("inicio") LocalDate inicio,
                                    @Param("fim") LocalDate fim);

    @Query("""
           select p.tipoCliente as tipo, count(p) as total
           from Pesquisa p
           where p.tipoPesquisa = :tipo
             and p.dataPesquisa between :inicio and :fim
           group by p.tipoCliente
           """)
    List<TipoClienteCount> contarPorTipoClienteDentroDoTipoPesquisa(@Param("tipo") TipoPesquisa tipo,
                                                                    @Param("inicio") LocalDate inicio,
                                                                    @Param("fim") LocalDate fim);

    interface TipoClienteCount {
        TipoCliente getTipo();
        long getTotal();
    }
}
