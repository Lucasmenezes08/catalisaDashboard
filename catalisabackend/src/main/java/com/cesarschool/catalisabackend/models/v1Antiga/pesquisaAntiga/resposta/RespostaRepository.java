package com.cesarschool.catalisabackend.models.v1Antiga.pesquisaAntiga.resposta;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RespostaRepository extends JpaRepository<Resposta, Long> {

    List<Resposta> findByPesquisa_Id(Long pesquisaId);
    List<Resposta> findByPergunta_Id(Long perguntaId);

    Page<Resposta> findByPesquisa_Id(Long pesquisaId, Pageable pageable);
    Page<Resposta> findByPergunta_Id(Long perguntaId, Pageable pageable);

    // Uma resposta específica de uma pergunta dentro de uma pesquisa
    Optional<Resposta> findByPesquisa_IdAndPergunta_Id(Long pesquisaId, Long perguntaId);

    boolean existsByPesquisa_IdAndPergunta_Id(Long pesquisaId, Long perguntaId);

    long countByPesquisa_Id(Long pesquisaId);
    long countByPergunta_Id(Long perguntaId);

    long deleteByPesquisa_Id(Long pesquisaId);
    long deleteByPergunta_Id(Long perguntaId);

}