package com.cesarschool.catalisabackend.models.consumo;

import com.cesarschool.catalisabackend.models.utils.ListaString;
import com.cesarschool.catalisabackend.models.utils.ResultService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ConsumoService {
    private final ConsumoRepository consumoRepository;
    public ConsumoService(ConsumoRepository consumoRepository) {
        this.consumoRepository = consumoRepository;
    }
    public ResultService include(Consumo consumo) {
        ResultService result = validate(consumo);
        if(result.isValid()){
            if(search(consumo.getId()) == null){
                result.setRealized(true);
                consumoRepository.save(consumo);
            }
            else{
                result.addError("Já consumido");
            }
        }
        return result;
    }

    public ResultService update(long id, Consumo consumo){
        Consumo objeto = search(id);
        ResultService result = validate(consumo);
        if(objeto != null){
            if(result.isValid()){
                objeto.setUser(consumo.getUser());
                objeto.setProduct(consumo.getProduct());
                objeto.setDataConsumo(consumo.getDataConsumo());
                objeto.setAvaliacao(consumo.getAvaliacao());
                objeto.setPesquisaRespondida(consumo.isPesquisaRespondida());
                objeto.setPesquisa(consumo.getPesquisa());
                consumoRepository.save(objeto);
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
        Consumo consumo = search(id);
        if(consumo != null){
            consumoRepository.delete(consumo);
            return new ResultService(true, true, null);
        }
        ListaString listaString = new ListaString();
        listaString.adicionar("Consumo não encontrado");
        return new ResultService(false, false, listaString);
    }

    public Consumo search(long id){
        return consumoRepository.findById(id).orElse(null);
    }

    public List<Consumo> findAll() {
        return consumoRepository.findAll();
    }

    public ResultService validate(Consumo consumo){
        ListaString erros = new ListaString();
        if(consumo == null){
            erros.adicionar("Consumo inexistente");
            return new ResultService(false,false,erros);
        }
        boolean valido = true;
        boolean realizado = false;
        if(consumo.getUser() == null){
            valido = false;
            erros.adicionar("Usuario indefinido Inexistente");
        }
        if(consumo.getProduct() == null){
            valido = false;
            erros.adicionar("Produto Consumido Inexistente");
        }
        if(consumo.getDataConsumo() == null || LocalDate.now().isAfter(consumo.getDataConsumo())) {
            valido = false;
            erros.adicionar("Data de Consumo Inexistente ou maior que a atual");
        }
        if(consumo.getPesquisa() == null){
            valido = false;
            erros.adicionar("Pesquisa Inexistente");
        }
        if(consumo.isPesquisaRespondida()){
            if(consumo.getAvaliacao() < 0 || consumo.getAvaliacao() > 5) {
                valido = false;
                erros.adicionar("Não foi atribuida uma nota valida");
            }
        }
        else{
            erros.adicionar("Pesquisa Não respondida");
        }

        return new ResultService(valido, realizado, erros);
    }
}
