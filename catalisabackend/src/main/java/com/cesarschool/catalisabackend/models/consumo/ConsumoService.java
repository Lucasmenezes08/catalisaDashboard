package com.cesarschool.catalisabackend.models.consumo;

import com.cesarschool.catalisabackend.models.user.User;
import com.cesarschool.catalisabackend.models.utils.ListaString;
import com.cesarschool.catalisabackend.models.utils.ResultService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;

@Service
@Transactional
public class ConsumoService {
    private final ConsumoRepository consumoRepository;
    public ConsumoService(ConsumoRepository consumoRepository) {
        this.consumoRepository = consumoRepository;
    }

    public Consumo findByUser(long userId) {
        return consumoRepository
                .findTopByUser_IdOrderByDataConsumoDesc(userId)
                .orElse(null);
    }
    public Consumo findById(Long id){
        return consumoRepository.findById(id).orElse(null);
    }

    public ResultService create(Consumo consumo){
        ResultService result = validate(consumo);
        if (result.isValid()) {
            consumoRepository.save(consumo);
            result.setRealized(true);
        }
        return result;
    }
    public ResultService update(long id, Consumo consumo){
        ResultService result = validate(consumo);
        if (!result.isValid()) {
            return result;
        }

        Consumo consumoUpdate = findById(id);
        if (consumoUpdate == null) {
            result.addError("Consumo nao encontrado ou não existente");
            return result;
        }

        consumoUpdate.setUser(consumo.getUser());
        consumoUpdate.setDataConsumo(consumo.getDataConsumo());
        consumoUpdate.setPesquisa(consumo.getPesquisa());
        consumoUpdate.setConsumiuPesquisa(consumo.isConsumiuPesquisa());
        consumoUpdate.setProduto(consumo.getProduto());

        consumoRepository.save(consumoUpdate);
        result.setRealized(true);
        return result;
    }

    public ResultService delete(long id){
        Consumo consumo = consumoRepository.findById(id).orElse(null);
        ListaString listaString = new ListaString();
        if(consumo != null){
            consumoRepository.delete(consumo);
            return new ResultService(true, true, null);
        }
        listaString.adicionar("Consumo não encontrado");
        return new ResultService(false, false, listaString);
    }
    private ResultService validate(Consumo consumo){
        ListaString erros = new ListaString();
        if(consumo == null){
            erros.adicionar("Consumo inexistente");
            return new ResultService(false, false, erros);
        }
        boolean valido = true;
        boolean realizado = false;

        if(consumo.getUser() == null){
            erros.adicionar("Usuario inexistente ou não atribuido");
            valido = false;
        }
        if(consumo.getProduto() == null){
            erros.adicionar("Produto inexistente ou não atribuido");
            valido = false;
        }
//        if(consumo.getPesquisa() == null){
//            erros.adicionar("Pesquisa inexistente");
//            valido = false;
//        }
        if(consumo.getDataConsumo() == null){
            erros.adicionar("DataConsumo inexistente ou não atribuida");
            valido = false;
        }
//        LocalDate hoje = LocalDate.now(ZoneId.systemDefault());
//        if (consumo.getDataConsumo().isAfter(hoje)) {
//            erros.adicionar("Data do consumo no futuro");
//            valido = false;
//        }
        return new ResultService(valido, realizado, erros);
    }
}
