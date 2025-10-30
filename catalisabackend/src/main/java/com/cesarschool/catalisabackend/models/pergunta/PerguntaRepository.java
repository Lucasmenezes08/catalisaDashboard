package com.cesarschool.catalisabackend.models.pergunta;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PerguntaRepository extends JpaRepository<Pergunta, Long> {
    Optional<Pergunta> findByTexto(String texto);
    List<Pergunta> findByTextoContaining(String texto);
    List<Pergunta> findByTextoStartingWith(String texto);
    Optional<Pergunta> findByTextoIgnoreCase(String texto);
    List<Pergunta> findByTextoContainingIgnoreCase(String texto);
    List<Pergunta> findByTextoStartingWithIgnoreCase(String texto);
    Optional<Pergunta> findById(long id);
    List<Pergunta> findByTipo(PerguntaTipo tipo);
    Optional<Pergunta> deleteByTipo(PerguntaTipo tipo);
    List<Pergunta> findAllByTipo(PerguntaTipo tipo);
    List<Pergunta> findAllByTipoOrderByTextoAsc(PerguntaTipo tipo);
    List<Pergunta> findAllByTexto(String texto);
    List<Pergunta> findAllByTextoContaining(String texto);
    List<Pergunta> findAllByTextoStartingWith(String texto);
    List<Pergunta> findAllByTextoIgnoreCase(String texto);
    List<Pergunta> findAllByTextoContainingIgnoreCase(String texto);
    List<Pergunta> findAllByTextoStartingWithIgnoreCase(String texto);
    boolean existsByTexto(String texto);
    boolean existsByTextoIgnoreCase(String texto);
    boolean existsByTextoContaining(String texto);
    boolean existsAllByTexto(String texto);
}
