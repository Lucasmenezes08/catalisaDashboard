package com.cesarschool.catalisabackend.models.v1Antiga.pesquisaAntiga;

import com.cesarschool.catalisabackend.models.v1Antiga.pesquisaAntiga.consumoAntigo.ConsumoAntigo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PesquisaRepositoryAntigo extends JpaRepository<PesquisaAntiga, Long> {
    PesquisaAntiga searchByConsumo(ConsumoAntigo consumoAntigo);
    List<PesquisaAntiga> findByConsumo_Id(Long consumoId);
    Page<PesquisaAntiga> findByConsumo_Id(Long consumoId, Pageable pageable);
    long countByConsumo_Id(Long consumoId);
    boolean existsByConsumo_Id(Long consumoId);
    long deleteByConsumo_Id(Long consumoId);

    List<PesquisaAntiga> findByPerguntas_Id(Long perguntaId);
    Page<PesquisaAntiga> findByPerguntas_Id(Long perguntaId, Pageable pageable);
    long countByPerguntas_Id(Long perguntaId);
    boolean existsByPerguntas_Id(Long perguntaId);

    List<PesquisaAntiga> findByRespostas_Id(Long respostaId);
    Page<PesquisaAntiga> findByRespostas_Id(Long respostaId, Pageable pageable);
    long countByRespostas_Id(Long respostaId);
    boolean existsByRespostas_Id(Long respostaId);
}
