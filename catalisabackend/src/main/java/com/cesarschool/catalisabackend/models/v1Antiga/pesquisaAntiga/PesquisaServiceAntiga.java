package com.cesarschool.catalisabackend.models.v1Antiga.pesquisaAntiga;

import com.cesarschool.catalisabackend.models.v1Antiga.pesquisaAntiga.consumoAntigo.ConsumoAntigo;
import com.cesarschool.catalisabackend.models.utils.ListaString;
import com.cesarschool.catalisabackend.models.utils.ResultService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PesquisaServiceAntiga {
    private final PesquisaRepositoryAntigo pesquisaRepositoryAntigo;

    public PesquisaServiceAntiga(PesquisaRepositoryAntigo pesquisaRepositoryAntigo) {
        this.pesquisaRepositoryAntigo = pesquisaRepositoryAntigo;
    }

    public ResultService include(PesquisaAntiga pesquisaAntiga) {
        ResultService result = validate(pesquisaAntiga);
        if (result.isValid()) {
            if(search(pesquisaAntiga.getConsumoAntigo()) == null){
                pesquisaRepositoryAntigo.save(pesquisaAntiga);
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
    public ResultService update(long id, PesquisaAntiga dados) {
        ResultService result = validate(dados);
        if (!result.isValid()) return result;

        PesquisaAntiga atual = search(id);
        if (atual == null) {
            result.addError("Pesquisa inexistente");
            return result;
        }

        atual.setConsumoAntigo(dados.getConsumoAntigo());
        atual.setPerguntas(dados.getPerguntas());

        atual.getRespostas().clear();
        if (dados.getRespostas() != null) {
            dados.getRespostas().forEach(r -> {
                r.setPesquisaAntiga(atual);
                atual.getRespostas().add(r);
            });
        }

        pesquisaRepositoryAntigo.save(atual);
        result.setRealized(true);
        return result;
    }
    public ResultService delete(long id){
        PesquisaAntiga pesquisaAntiga = search(id);
        if(pesquisaAntiga == null){
            ListaString listaString = new ListaString();
            listaString.adicionar("Pesquisa inexistente");
            return new ResultService(false,false, listaString);
        }
        else{
            pesquisaRepositoryAntigo.delete(pesquisaAntiga);
            return new ResultService(true,true,null);
        }
    }
    public List<PesquisaAntiga> findAll(){
        return pesquisaRepositoryAntigo.findAll();
    }
    public PesquisaAntiga search(long id){
        return pesquisaRepositoryAntigo.findById(id).orElse(null);
    }
    public PesquisaAntiga search(ConsumoAntigo consumoAntigo){
        return pesquisaRepositoryAntigo.searchByConsumo(consumoAntigo);
    }
    public ResultService validate(PesquisaAntiga pesquisaAntiga){
        ListaString erros = new ListaString();
        if(pesquisaAntiga == null){
            erros.adicionar("Pesquisa inexistente");
            return new ResultService(false,false,erros);
        }
        boolean valid = true;
        boolean realized = false;

        if(pesquisaAntiga.getConsumoAntigo() == null){
            erros.adicionar("Consumo inexistente");
            valid = false;
        }
        if (pesquisaAntiga.getPerguntas() == null){
            erros.adicionar("Perguntas inexistente");
            valid = false;
        }
        if(pesquisaAntiga.getRespostas() == null){
            erros.adicionar("Respostas inexistente");
            valid = false;
        }
        return new ResultService(valid, realized, erros);
    }
}
