package com.cesarschool.catalisabackend.models.v1Antiga.pesquisaAntiga.consumoAntigo;

import com.cesarschool.catalisabackend.models.utils.ListaString;
import com.cesarschool.catalisabackend.models.utils.ResultService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ConsumoServiceAntigo {
    private final ConsumoRepositoryAntigo consumoRepositoryAntigo;
    public ConsumoServiceAntigo(ConsumoRepositoryAntigo consumoRepositoryAntigo) {
        this.consumoRepositoryAntigo = consumoRepositoryAntigo;
    }
    public ResultService include(ConsumoAntigo consumoAntigo) {
        ResultService result = validate(consumoAntigo);
        if(result.isValid()){
            if(search(consumoAntigo.getId()) == null){
                result.setRealized(true);
                consumoRepositoryAntigo.save(consumoAntigo);
            }
            else{
                result.addError("Já consumido");
            }
        }
        return result;
    }

    public ResultService update(long id, ConsumoAntigo consumoAntigo){
        ConsumoAntigo objeto = search(id);
        ResultService result = validate(consumoAntigo);
        if(objeto != null){
            if(result.isValid()){
                objeto.setUser(consumoAntigo.getUser());
                objeto.setProduct(consumoAntigo.getProduct());
                objeto.setDataConsumo(consumoAntigo.getDataConsumo());
                objeto.setAvaliacao(consumoAntigo.getAvaliacao());
                objeto.setPesquisaRespondida(consumoAntigo.isPesquisaRespondida());
                objeto.setPesquisaAntiga(consumoAntigo.getPesquisaAntiga());
                consumoRepositoryAntigo.save(objeto);
                result.setRealized(true);
            }
        }
        else{
            ListaString listaString = new ListaString();
            listaString.adicionar("Identificador do Consumo não encontrado");
            return new ResultService(false, false, listaString);
        }
        return result;
    }

    public ResultService delete(long id){
        ConsumoAntigo consumoAntigo = search(id);
        if(consumoAntigo != null){
            consumoRepositoryAntigo.delete(consumoAntigo);
            return new ResultService(true, true, null);
        }
        ListaString listaString = new ListaString();
        listaString.adicionar("Consumo não encontrado");
        return new ResultService(false, false, listaString);
    }

    public ConsumoAntigo search(long id){
        return consumoRepositoryAntigo.findById(id).orElse(null);
    }

    public List<ConsumoAntigo> findAll() {
        return consumoRepositoryAntigo.findAll();
    }

    public ResultService validate(ConsumoAntigo consumoAntigo){
        ListaString erros = new ListaString();
        if(consumoAntigo == null){
            erros.adicionar("Consumo inexistente");
            return new ResultService(false,false,erros);
        }
        boolean valido = true;
        boolean realizado = false;
        if(consumoAntigo.getUser() == null){
            valido = false;
            erros.adicionar("Usuario indefinido Inexistente");
        }
        if(consumoAntigo.getProduct() == null){
            valido = false;
            erros.adicionar("Produto Consumido Inexistente");
        }

        if (consumoAntigo.getDataConsumo() == null) {
            valido = false;
            erros.adicionar("Data de consumo obrigatória");
        }
        else if (consumoAntigo.getDataConsumo().isAfter(LocalDate.now())) {
            valido = false;
            erros.adicionar("Data de consumo no futuro não é permitida");
        }

        if (consumoAntigo.isPesquisaRespondida()) {
            if (consumoAntigo.getPesquisaAntiga() == null) {
                valido = false;
                erros.adicionar("Informe a pesquisa ao marcar 'pesquisaRespondida=true'");
            }
            if (consumoAntigo.getAvaliacao() < 1 || consumoAntigo.getAvaliacao() > 5) {
                valido = false;
                erros.adicionar("Avaliacao deve estar entre 1 e 5 quando a pesquisa é respondida");
            }
        }

        return new ResultService(valido, realizado, erros);
    }
}
