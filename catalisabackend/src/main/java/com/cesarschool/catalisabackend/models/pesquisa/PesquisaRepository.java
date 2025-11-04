package com.cesarschool.catalisabackend.models.pesquisa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PesquisaRepository extends JpaRepository<Pesquisa, Long> {

    List<Pesquisa> findByConsumo_Id(Long consumoId);
    Page<Pesquisa> findByConsumo_Id(Long consumoId, Pageable pageable);
    long countByConsumo_Id(Long consumoId);
    boolean existsByConsumo_Id(Long consumoId);
    long deleteByConsumo_Id(Long consumoId);

    List<Pesquisa> findByPerguntas_Id(Long perguntaId);
    Page<Pesquisa> findByPerguntas_Id(Long perguntaId, Pageable pageable);
    long countByPerguntas_Id(Long perguntaId);
    boolean existsByPerguntas_Id(Long perguntaId);

    List<Pesquisa> findByRespostas_Id(Long respostaId);
    Page<Pesquisa> findByRespostas_Id(Long respostaId, Pageable pageable);
    long countByRespostas_Id(Long respostaId);
    boolean existsByRespostas_Id(Long respostaId);
}
