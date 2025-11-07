package com.cesarschool.catalisabackend.models.pesquisa;

import com.cesarschool.catalisabackend.models.consumo.Consumo;
import com.cesarschool.catalisabackend.models.utils.ListaString;
import com.cesarschool.catalisabackend.models.utils.ResultService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PesquisaService {
    private final PesquisaRepository pesquisaRepository;
    public PesquisaService(PesquisaRepository pesquisaRepository) {
        this.pesquisaRepository = pesquisaRepository;
    }
    public ResultService createPesquisa() {}
    public ResultService deletePesquisa(long id) {}
    public ResultService updatePesquisa(long id, Pesquisa pesquisa){}
    public Pesquisa getPesquisa(Consumo consumo){
        return pesquisaRepository.findByConsumo(consumo).orElse(null);
    }
    public Pesquisa getPesquisa(long id){
        return pesquisaRepository.findById(id).orElse(null);
    }
    public ResultService validate(Pesquisa pesquisa){
        ListaString erros = new ListaString();
        if(pesquisa == null){
            erros.adicionar("Pesquisa não informada");
            return new ResultService(false,false,erros);
        }
        boolean valido = true;
        boolean realizado = false;
        if(pesquisa.getConsumo() == null){
            valido = false;
            erros.adicionar("Não há consumo associado");
        }
        //fazer consumos depois
        Consumo consumo = pesquisa.getConsumo();

        if(pesquisa.getDataPesquisa() == null){
            erros.adicionar("Data da pesquisa não informada");
            valido = false;
        }
        else if(pesquisa.getDataPesquisa().isAfter(LocalDate.now())){
            valido = false;
            erros.adicionar("Data da pesquisa superior a data atual");
        }

        if (pesquisa.getTipoPesquisa() == null){
            valido = false;
            erros.adicionar("Tipo da pesquisa não informada");
        }

    }

}
