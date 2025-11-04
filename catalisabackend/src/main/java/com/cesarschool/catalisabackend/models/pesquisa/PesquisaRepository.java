package com.cesarschool.catalisabackend.models.pesquisa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PesquisaRepository extends JpaRepository<Pesquisa,Long> {

}
