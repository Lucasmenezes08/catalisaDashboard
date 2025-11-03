package com.cesarschool.catalisabackend.models.resposta;

import com.cesarschool.catalisabackend.models.pesquisa.Pesquisa;
import com.cesarschool.catalisabackend.models.utils.ListaString;
import com.cesarschool.catalisabackend.models.utils.ResultService;
import com.cesarschool.catalisabackend.models.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RespostaService {
    private final RespostaRepository respostaRepository;
    public RespostaService(RespostaRepository respostaRepository) {
        this.respostaRepository = respostaRepository;
    }
    public ResultService include(Resposta resposta) {
        ListaString erros = new ListaString();
        if (resposta == null) {
            erros.adicionar("Resposta informada nula");
            return new ResultService(false, false, erros);
        }
        ResultService result = validate(resposta);
        if (result.isValid()) {
            if(search(resposta.getId()) == null){
                result.setRealized(true);
                respostaRepository.save(resposta);
            }
            else{
                result.addError("Resposta ja existente");
            }
        }
        return result;
    }
    public ResultService update(long id, Resposta resposta) {
        if (resposta == null) {
            ListaString erros = new ListaString();
            erros.adicionar("Resposta informada nula");
            return new ResultService(false, false, erros);
        }
        ResultService result = validate(resposta);
        if (result.isValid()) {
            if(search(resposta.getId()) != null){
                Resposta atual = respostaRepository.findById(id).orElse(null);
                atual.setResposta(resposta.getResposta());
                atual.setPergunta(resposta.getPergunta());
                atual.setPesquisa(resposta.getPesquisa());
                respostaRepository.save(atual);
                result.setRealized(true);
            }
            else{
                result.addError("Resposta nao existente");
            }
        }
        return result;
    }
    public ResultService delete(long id) {
        Resposta resposta = search(id);
        if (resposta == null) {
            ListaString listaString = new ListaString();
            listaString.adicionar("Resposta inexistente");
            return new ResultService(false, false, listaString);
        }
        else{
            respostaRepository.delete(resposta);
            return new ResultService(true, true, null);
        }
    }
    public Resposta search(long id){
        return respostaRepository.findById(id).orElse(null);
    }
    public ResultService validate(Resposta resposta){
        ListaString erros = new ListaString();
        if(resposta == null){
            erros.adicionar("Resposta inexistente");
            return new ResultService(false,false,erros);
        }
        boolean valido = true;
        boolean realized = false;
        if(resposta.getPesquisa() == null){
            valido = false;
            erros.adicionar("Pesquisa inexistente");
        }
        if(resposta.getPergunta() == null){
            valido = false;
            erros.adicionar("Pergunta inexistente");
        }
        if(StringUtils.estaVazia(resposta.getResposta())){
            valido = false;
            erros.adicionar("Resposta inexistente ou vazia");
        }
        return new ResultService(valido,realized,erros);
    }
}
