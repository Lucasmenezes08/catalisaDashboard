package com.cesarschool.catalisabackend.models.pesquisa;

import com.cesarschool.catalisabackend.models.consumo.Consumo;
import com.cesarschool.catalisabackend.models.product.Product;
import com.cesarschool.catalisabackend.models.utils.ListaString;
import com.cesarschool.catalisabackend.models.utils.ResultService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PesquisaService {
    private final PesquisaRepository pesquisaRepository;

    public PesquisaService(PesquisaRepository pesquisaRepository) {
        this.pesquisaRepository = pesquisaRepository;
    }

    public ResultService include(Pesquisa pesquisa) {
        ResultService result = validate(pesquisa);
        if (result.isValid()) {
            if(search(pesquisa.getConsumo()) == null){
                pesquisaRepository.save(pesquisa);
                result.setRealized(true);
            }
            else{
                result.setRealized(false);
                result.addError("Pesquisa já existente");
            }
        }
        return result;
    }
//    public ResultService update(long id, Pesquisa pesquisa) {
//        ResultService result = validate(pesquisa);
//        if (result.isValid()) {
//            Pesquisa pesquisaUpdate = search(id);
//            if(pesquisaUpdate != null){
//                pesquisaUpdate.setConsumo(pesquisa.getConsumo());
//                pesquisaUpdate.setPerguntas(pesquisa.getPerguntas());
//                pesquisaUpdate.setRespostas(pesquisa.getRespostas());
//                pesquisaRepository.save(pesquisaUpdate);
//                result.setRealized(true);
//            }
//            else{
//                result.addError("Pesquisa inexistente");
//            }
//        }
//        return result;
//    }
    public ResultService update(long id, Pesquisa dados) {
        ResultService result = validate(dados);
        if (!result.isValid()) return result;

        Pesquisa atual = search(id);
        if (atual == null) {
            result.addError("Pesquisa inexistente");
            return result;
        }

        atual.setConsumo(dados.getConsumo());
        atual.setPerguntas(dados.getPerguntas());

        atual.getRespostas().clear();
        if (dados.getRespostas() != null) {
            dados.getRespostas().forEach(r -> {
                r.setPesquisa(atual);
                atual.getRespostas().add(r);
            });
        }

        pesquisaRepository.save(atual);
        result.setRealized(true);
        return result;
    }
    public ResultService delete(long id){
        Pesquisa pesquisa = search(id);
        if(pesquisa == null){
            ListaString listaString = new ListaString();
            listaString.adicionar("Pesquisa inexistente");
            return new ResultService(false,false, listaString);
        }
        else{
            pesquisaRepository.delete(pesquisa);
            return new ResultService(true,true,null);
        }
    }
    public List<Pesquisa> findAll(){
        return pesquisaRepository.findAll();
    }
    public Pesquisa search(long id){
        return pesquisaRepository.findById(id).orElse(null);
    }
    public Pesquisa search(Consumo consumo){
        return pesquisaRepository.searchByConsumo(consumo);
    }
    public ResultService validate(Pesquisa pesquisa){
        ListaString erros = new ListaString();
        if(pesquisa == null){
            erros.adicionar("Pesquisa inexistente");
            return new ResultService(false,false,erros);
        }
        boolean valid = true;
        boolean realized = false;

        if(pesquisa.getConsumo() == null){
            erros.adicionar("Consumo inexistente");
            valid = false;
        }
        if (pesquisa.getPerguntas() == null){
            erros.adicionar("Perguntas inexistente");
            valid = false;
        }
        if(pesquisa.getRespostas() == null){
            erros.adicionar("Respostas inexistente");
            valid = false;
        }
        return new ResultService(valid, realized, erros);
    }
}
