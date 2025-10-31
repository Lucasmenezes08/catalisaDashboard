package com.cesarschool.catalisabackend.models.resposta;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RespostaRepository extends JpaRepository<Resposta, Long> {
    public List<Resposta> findAll();
    public Optional<Resposta> findById(Long id);

}
