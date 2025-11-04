package com.cesarschool.catalisabackend.models.pesquisa;

import org.springframework.stereotype.Service;

@Service
public class PesquisaService {
    private final PesquisaRepository pesquisaRepository;
    public PesquisaService(PesquisaRepository pesquisaRepository) {
        this.pesquisaRepository = pesquisaRepository;
    }
}
