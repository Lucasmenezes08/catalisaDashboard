package com.cesarschool.catalisabackend.models.pesquisa;

import com.cesarschool.catalisabackend.models.consumo.Consumo;
import com.cesarschool.catalisabackend.models.user.User;
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
    public ResultService createPesquisa(Pesquisa pesquisa) {
        ResultService result = validate(pesquisa);
        if(result.isValid()){
            if(getPesquisa(pesquisa.getConsumo()) == null){
                pesquisaRepository.save(pesquisa);
                Consumo consumo = pesquisa.getConsumo();
                consumo.setPesquisa(pesquisa);
                consumo.setConsumiuPesquisa(true);
                result.setRealized(true);
            }
            else{
                result.addError("Pesquisa ´ja existente");
            }
        }
        return result;
    }
    public ResultService deletePesquisa(long id) {
        Pesquisa pesquisa = getPesquisa(id);
        if(pesquisa == null){
            ListaString listaString = new ListaString();
            listaString.adicionar("Pesquisa não encontrada");
            return new ResultService(false, false, listaString);
        }
        else{
            pesquisaRepository.delete(pesquisa);
            return new ResultService(true, true, null);
        }
    }
    public ResultService updatePesquisa(long id, Pesquisa pesquisa){
        ListaString erros = new ListaString();
        if(pesquisa == null){
            erros.adicionar("Não foi informada uma nova pesquisa");
        }
        Pesquisa pesquisaUpdate = getPesquisa(id);
        ResultService pesquisaValida = validate(pesquisa);
        if(pesquisaValida.isValid()){
            if(pesquisaUpdate != null){
                pesquisaUpdate.setConsumo(pesquisa.getConsumo());
                pesquisaUpdate.setResposta(pesquisa.getResposta());
                pesquisaUpdate.setDataPesquisa(pesquisa.getDataPesquisa());
                pesquisaUpdate.recalcularNotaPesquisa(pesquisa.getNota(), pesquisa.getTipoPesquisa());
                pesquisaRepository.save(pesquisaUpdate);
                pesquisaValida.setRealized(true);
            }
            else{
                pesquisaValida.addError("Pesquisa para atualizar inexistente");
            }
        }
        return pesquisaValida;
    }
    public User getUser(Pesquisa pesquisa){
        if(pesquisa == null) throw new RuntimeException("Pesquisa inexistente");
        User user = pesquisa.getConsumo().getUser();
        if(user == null) throw new RuntimeException("Não existe usuario associado a pesquisa");
        return user;
    }
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
        Consumo consumo = pesquisa.getConsumo();
        if(consumo.getUser() == null){
            valido = false;
            erros.adicionar("Usuario do consumo inexistente");
        }
//        if(!consumo.isConsumiuPesquisa()){
//            valido = false;
//            erros.adicionar("Usuario não respondeu a pesquisa");
//        }
        if(consumo.getProduto() == null){
            valido = false;
            erros.adicionar("Produto relacionado a pesquisa é inexistente ou não relacionado");
        }
        if(pesquisa.getDataPesquisa() == null){
            erros.adicionar("Data da pesquisa não informada");
            valido = false;
        }
//        else if(pesquisa.getDataPesquisa().isAfter(LocalDate.now())){
//            valido = false;
//            erros.adicionar("Data da pesquisa superior a data atual");
//        }

        if (pesquisa.getTipoPesquisa() == null){
            valido = false;
            erros.adicionar("Tipo da pesquisa não informada");
        }
        else{
            if (pesquisa.getTipoPesquisa() == TipoPesquisa.NPS && (pesquisa.getNota() > 10 || pesquisa.getNota() < 0)) {
                valido = false;
                erros.adicionar("Nota superior ou inferior ao range de notas da pesquisa: NPS");
            }
            else if(pesquisa.getTipoPesquisa() == TipoPesquisa.CSAT && (pesquisa.getNota() > 5 || pesquisa.getNota() < 0)) {
                valido = false;
                erros.adicionar("Nota superior ou inferior ao range de notas da pesquisa: CSAT");
            }
        }
        return new ResultService(valido,realizado,erros);
    }
}
