package com.cesarschool.catalisabackend.models.consumo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface ConsumoRepository extends JpaRepository<Consumo, Long> {
    List<Consumo> getAll();
    List<Consumo> findByUser_Id(Long userId);
    Page<Consumo> findByUser_Id(Long userId, org.springframework.data.domain.Pageable pageable);

    List<Consumo> findByProduto_Id(Long productId);
    Page<Consumo> findByProduto_Id(Long productId, org.springframework.data.domain.Pageable pageable);

    Optional<Consumo> findTopByUser_IdOrderByDataConsumoDesc(Long userId);

    Page<Consumo> findByDataConsumoBetween(LocalDate inicio, LocalDate fim, org.springframework.data.domain.Pageable pageable);
    List<Consumo> findByDataConsumoBetween(LocalDate inicio, LocalDate fim);

    Page<Consumo> findByPesquisaIsNotNull(org.springframework.data.domain.Pageable pageable);
    Page<Consumo> findByPesquisaIsNull(org.springframework.data.domain.Pageable pageable);

    boolean existsByUser_IdAndProduto_IdAndDataConsumo(Long userId, Long productId, LocalDate data);

    long countByUser_Id(Long userId);
    long countByUser_IdAndDataConsumoBetween(Long userId, LocalDate inicio, LocalDate fim);

    long deleteByUser_Id(Long userId);
    long deleteByProduto_Id(Long productId);

    Page<Consumo> findByUser_IdAndDataConsumoBetween(Long userId, LocalDate inicio, LocalDate fim, Pageable pageable);

    // (opcional) com EntityGraph:
    // @EntityGraph(attributePaths = {"user","produto"})
    // Page<Consumo> findAll(org.springframework.data.domain.Pageable pageable);
}

