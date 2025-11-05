package com.cesarschool.catalisabackend.models.pergunta;


import com.cesarschool.catalisabackend.models.product.Product;
import org.springframework.stereotype.Service;
import com.cesarschool.catalisabackend.models.utils.StringUtils;
import com.cesarschool.catalisabackend.models.utils.ResultService;
import java.util.List;
import java.util.Optional;

import com.cesarschool.catalisabackend.models.utils.ListaString;
import jakarta.transaction.Transactional;

@Service
public class PerguntaService {

    private final PerguntaRepository perguntaRepository;

    public PerguntaService(PerguntaRepository perguntaRepository) {
        this.perguntaRepository = perguntaRepository;
    }

    public Pergunta search(long id) {
        return perguntaRepository.findById(id).orElse(null);
    }

    public  List<Pergunta> findAll() {
        return perguntaRepository.findAll();
    }

    public Pergunta search(String texto){
        return perguntaRepository.findByTextoIgnoreCase(texto).orElse(null);
    }

    public ResultService include(Pergunta pergunta) {
        ResultService result = validate(pergunta);
        if(result.isValid()){
            if(search(pergunta.getTexto())==null){
                result.setRealized(true);
                perguntaRepository.save(pergunta);
            }
            else{
                result.addError("Pergunta já existente");
            }
        }
        return result;
    }
    @Transactional
    public ResultService updateById(long id, Pergunta dados) {
        ListaString erros = new ListaString();

        Optional<Pergunta> optExisting = perguntaRepository.findById(id);
        Pergunta existing = optExisting.orElse(null);
        if (existing == null) {
            erros.adicionar("Pergunta inexistente");
            return new ResultService(false, false, erros);
        }

        ResultService valid = validate(dados);
        if (!valid.isValid()) {
            return valid;
        }
        existing.setTexto(dados.getTexto());
        existing.setTipo(dados.getTipo());
        existing.setNotaMinima(dados.getNotaMinima());
        existing.setNotaMaxima(dados.getNotaMaxima());

        perguntaRepository.save(existing);

        return new ResultService(true, true, new ListaString());
    }

    public ResultService updateByTexto(String textoAlvo, Pergunta dados) {
        ListaString erros = new ListaString();

        Pergunta existing = search(textoAlvo);
        if (existing == null) {
            erros.adicionar("Pergunta inexistente");
            return new ResultService(false, false, erros);
        }

        ResultService valid = validate(dados);
        if (!valid.isValid()) {
            return valid;
        }

        if (dados.getTexto() != null &&
                !dados.getTexto().equalsIgnoreCase(textoAlvo) &&
                perguntaRepository.existsByTextoIgnoreCase(dados.getTexto())) {
            erros.adicionar("Texto já existente");
            return new ResultService(false, false, erros);
        }

        existing.setTexto(dados.getTexto());
        existing.setTipo(dados.getTipo());
        existing.setNotaMinima(dados.getNotaMinima());
        existing.setNotaMaxima(dados.getNotaMaxima());

        perguntaRepository.save(existing);

        return new ResultService(true, true, new ListaString());
    }

    @Transactional
    public ResultService delete(long id) {
        var erros = new ListaString();
        if (id <= 0) {
            erros.adicionar("Id inválido");
            return new ResultService(false, false, erros);
        }
        if (!perguntaRepository.existsById(id)) {
            erros.adicionar("Pergunta inexistente");
            return new ResultService(true, false, erros);
        }
        perguntaRepository.deleteById(id);
        return new ResultService(true, true, erros);
    }

    public ResultService delete(String texto){
        ListaString erros = new ListaString();
        Pergunta  pergunta = search(texto);
        if(pergunta == null){
            erros.adicionar("Pergunta inexistente");
            return new ResultService(false, false, erros);
        }
        perguntaRepository.delete(pergunta);
        return new ResultService(true, true, erros);
    }

    public ResultService validate(Pergunta pergunta){
        ListaString erros = new ListaString();
        if(pergunta == null){
            erros.adicionar("Pergunta inexistente");
            return new ResultService(false, false, erros);
        }
        boolean valid = true;
        boolean realized = false;

        if(pergunta.getTipo() == null){
            valid = false;
            erros.adicionar("Tipo inexistente");
        }
        if (StringUtils.estaVazia(pergunta.getTexto())) {
            valid = false;
            erros.adicionar("Texto inexistente");
        }
        if(pergunta.getNotaMinima() < 1 ||  pergunta.getNotaMaxima() > 5){
            valid = false;
            erros.adicionar("Nota minima ou maxima invalida");
        }
        return new ResultService(valid, realized, erros);
    }
}
