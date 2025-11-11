package com.cesarschool.catalisabackend.models.consumo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface ConsumoRepository extends JpaRepository<Consumo, Long> {
    // --- Básico por usuário/produto ---
    Consumo findByUser_Id(Consumo consumo);
    List<Consumo> findByUser_Id(Long userId);
    Page<Consumo> findByUser_Id(Long userId, Pageable pageable);

    List<Consumo> findByProduto_Id(Long productId);
    Page<Consumo> findByProduto_Id(Long productId, Pageable pageable);

    // --- Intervalo de datas ---
    List<Consumo> findByDataConsumoBetween(LocalDate inicio, LocalDate fim);
    Page<Consumo> findByDataConsumoBetween(LocalDate inicio, LocalDate fim, Pageable pageable);

    // --- Combinações úteis ---
    List<Consumo> findByUser_IdAndDataConsumoBetween(Long userId, LocalDate inicio, LocalDate fim);

    // --- Últimos/mais recentes ---
    Optional<Consumo> findTopByUser_IdOrderByDataConsumoDesc(Long userId);

    // --- Flags/estado da pesquisa ---
    Page<Consumo> findByConsumiuPesquisaTrue(Pageable pageable);
    Page<Consumo> findByConsumiuPesquisaFalse(Pageable pageable);

    // Consumos que já possuem Pesquisa associada (1:1)
    Page<Consumo> findByPesquisaIsNotNull(Pageable pageable);
    Page<Consumo> findByPesquisaIsNull(Pageable pageable);

    // --- Existência (ex.: evitar duplicidade no mesmo dia) ---
    boolean existsByUser_IdAndProduto_IdAndDataConsumo(Long userId, Long productId, LocalDate data);

    // --- Contagens ---
    long countByUser_Id(Long userId);
    long countByUser_IdAndDataConsumoBetween(Long userId, LocalDate inicio, LocalDate fim);

    // --- Deletes direcionados (use com cuidado) ---
    long deleteByUser_Id(Long userId);
    long deleteByProduto_Id(Long productId);

    // --- (Opcional) Trazer relações em um único SELECT para evitar N+1 ---
    @EntityGraph(attributePaths = {"user", "produto"})
    Page<Consumo> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"user", "produto"})
    Page<Consumo> findByUser_IdAndDataConsumoBetween(Long userId, LocalDate inicio, LocalDate fim, Pageable pageable);
}
