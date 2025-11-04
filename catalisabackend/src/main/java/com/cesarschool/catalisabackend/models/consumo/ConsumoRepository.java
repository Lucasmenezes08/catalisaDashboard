package com.cesarschool.catalisabackend.models.consumo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConsumoRepository extends JpaRepository<Consumo, Long> {

    // ---- Lookups por usuário ----
    List<Consumo> findByUser_Id(Long userId);
    Page<Consumo> findByUser_Id(Long userId, Pageable pageable);
    long countByUser_Id(Long userId);
    long deleteByUser_Id(Long userId);

    // ---- Lookups por produto ----
    List<Consumo> findByProduct_Id(Long productId);
    Page<Consumo> findByProduct_Id(Long productId, Pageable pageable);
    long countByProduct_Id(Long productId);
    long deleteByProduct_Id(Long productId);

    // ---- Combinações usuário + produto ----
    List<Consumo> findByUser_IdAndProduct_Id(Long userId, Long productId);
    Page<Consumo> findByUser_IdAndProduct_Id(Long userId, Long productId, Pageable pageable);

    // Um consumo específico (ex.: por data)
    Optional<Consumo> findByUser_IdAndProduct_IdAndDataConsumo(Long userId, Long productId, LocalDate dataConsumo);
    boolean existsByUser_IdAndProduct_IdAndDataConsumo(Long userId, Long productId, LocalDate dataConsumo);

    // ---- Faixas de data ----
    List<Consumo> findByDataConsumoBetween(LocalDate start, LocalDate end);
    Page<Consumo> findByDataConsumoBetween(LocalDate start, LocalDate end, Pageable pageable);

    // ---- Flag de pesquisa respondida ----
    List<Consumo> findByPesquisaRespondida(boolean pesquisaRespondida);
    Page<Consumo> findByPesquisaRespondida(boolean pesquisaRespondida, Pageable pageable);

    // ---- Relação 1:1 com Pesquisa ----
    Optional<Consumo> findByPesquisa_Id(Long pesquisaId);
    boolean existsByPesquisa_Id(Long pesquisaId);

    // ---- Ordenações comuns ----
    List<Consumo> findAllByUser_IdOrderByDataConsumoDesc(Long userId);
    List<Consumo> findAllByProduct_IdOrderByDataConsumoDesc(Long productId);
}