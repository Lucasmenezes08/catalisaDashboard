package com.cesarschool.catalisabackend.models.v1Antiga.pesquisaAntiga.consumoAntigo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConsumoRepositoryAntigo extends JpaRepository<ConsumoAntigo, Long> {

    // ---- Lookups por usuário ----
    List<ConsumoAntigo> findByUser_Id(Long userId);
    Page<ConsumoAntigo> findByUser_Id(Long userId, Pageable pageable);
    long countByUser_Id(Long userId);
    long deleteByUser_Id(Long userId);

    // ---- Lookups por produto ----
    List<ConsumoAntigo> findByProduct_Id(Long productId);
    Page<ConsumoAntigo> findByProduct_Id(Long productId, Pageable pageable);
    long countByProduct_Id(Long productId);
    long deleteByProduct_Id(Long productId);

    // ---- Combinações usuário + produto ----
    List<ConsumoAntigo> findByUser_IdAndProduct_Id(Long userId, Long productId);
    Page<ConsumoAntigo> findByUser_IdAndProduct_Id(Long userId, Long productId, Pageable pageable);

    // Um consumo específico (ex.: por data)
    Optional<ConsumoAntigo> findByUser_IdAndProduct_IdAndDataConsumo(Long userId, Long productId, LocalDate dataConsumo);
    boolean existsByUser_IdAndProduct_IdAndDataConsumo(Long userId, Long productId, LocalDate dataConsumo);

    // ---- Faixas de data ----
    List<ConsumoAntigo> findByDataConsumoBetween(LocalDate start, LocalDate end);
    Page<ConsumoAntigo> findByDataConsumoBetween(LocalDate start, LocalDate end, Pageable pageable);

    // ---- Flag de pesquisa respondida ----
    List<ConsumoAntigo> findByPesquisaRespondida(boolean pesquisaRespondida);
    Page<ConsumoAntigo> findByPesquisaRespondida(boolean pesquisaRespondida, Pageable pageable);

    // ---- Relação 1:1 com Pesquisa ----
    Optional<ConsumoAntigo> findByPesquisa_Id(Long pesquisaId);
    boolean existsByPesquisa_Id(Long pesquisaId);

    // ---- Ordenações comuns ----
    List<ConsumoAntigo> findAllByUser_IdOrderByDataConsumoDesc(Long userId);
    List<ConsumoAntigo> findAllByProduct_IdOrderByDataConsumoDesc(Long productId);
}