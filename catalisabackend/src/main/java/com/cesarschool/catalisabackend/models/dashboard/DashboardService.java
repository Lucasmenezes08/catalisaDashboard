package com.cesarschool.catalisabackend.models.dashboard;

import com.cesarschool.catalisabackend.models.pesquisa.Pesquisa;
import com.cesarschool.catalisabackend.models.pesquisa.PesquisaRepository;
import com.cesarschool.catalisabackend.models.pesquisa.TipoCliente;
import com.cesarschool.catalisabackend.models.pesquisa.TipoPesquisa;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;
@Getter
@Service
public class DashboardService {
    private final PesquisaRepository pesquisaRepository;

    public DashboardService(PesquisaRepository pesquisaRepository) {
        this.pesquisaRepository = pesquisaRepository;
    }

    private List<Pesquisa> getTodasPesquisas() {
        return pesquisaRepository.findAll();
    }

    public int getNumeroPromotoresNPS(){
        if(getTodasPesquisas().isEmpty()) return 0;
        int contador = 0;
        for(Pesquisa pesquisa : getTodasPesquisas()){
            if(pesquisa.getTipoPesquisa() == TipoPesquisa.NPS && pesquisa.getTipoCliente() == TipoCliente.PROMOTOR) contador++;
        }
        return contador;
    }
    public int getNumeroNeutrosNPS(){
        if(getTodasPesquisas().isEmpty()) return 0;
        int contador = 0;
        for(Pesquisa pesquisa : getTodasPesquisas()){
            if(pesquisa.getTipoPesquisa() == TipoPesquisa.NPS && pesquisa.getTipoCliente() == TipoCliente.NEUTRO) contador++;
        }
        return contador;
    }
    public int getNumeroDetratoresNPS(){
        if(getTodasPesquisas().isEmpty()) return 0;
        int contador = 0;
        for(Pesquisa pesquisa : getTodasPesquisas()){
            if(pesquisa.getTipoPesquisa() == TipoPesquisa.NPS && pesquisa.getTipoCliente() == TipoCliente.DETRATOR) contador++;
        }
        return contador;
    }

    public int getPorcentagemNumeroPromotoresNPS(){
        if(getTodasPesquisas().isEmpty()) return 0;
        int promotores = getNumeroPromotoresNPS();
        int total = 0;
        for(Pesquisa pesquisa : getTodasPesquisas()){
            if(pesquisa.getTipoPesquisa() == TipoPesquisa.NPS) total++;
        }
        if (total == 0) return 0;
        return promotores * 100 / total;
    }
    public int getPorcentagemNumeroNeutrosNPS(){
        if(getTodasPesquisas().isEmpty()) return 0;
        int neutros = getNumeroNeutrosNPS();
        int total = 0;
        for(Pesquisa pesquisa : getTodasPesquisas()){
            if(pesquisa.getTipoPesquisa() == TipoPesquisa.NPS) total++;
        }
        if (total == 0) return 0;
        return neutros * 100 / total;
    }
    public int getPorcentagemNumeroDetratoresNPS(){
        if(getTodasPesquisas().isEmpty()) return 0;
        int detratores = getNumeroDetratoresNPS();
        int total = 0;
        for(Pesquisa pesquisa : getTodasPesquisas()){
            if(pesquisa.getTipoPesquisa() == TipoPesquisa.NPS) total++;
        }
        if (total == 0) return 0;
        return detratores * 100 / total;
    }

    public int getPorcentagemRespostasNPS(){
        if(getTodasPesquisas().isEmpty()) return 0;
        int contador = 0;
        int total = 0;
        for (Pesquisa pesquisa : getTodasPesquisas()) {
            if(pesquisa.getTipoPesquisa() == TipoPesquisa.NPS){
                total++;
                if(pesquisa.getResposta() != null){
                    contador++;
                }
            }

        }
        if (total == 0) return 0;
        return contador * 100 / total;
    }

    public int getNPS(){
        if(getTodasPesquisas().isEmpty()) return 0;
        int promotores = getPorcentagemNumeroPromotoresNPS();
        int detratores = getPorcentagemNumeroDetratoresNPS();
        return promotores - detratores;
    }


    public int getNota1CSAT() {
        int contador = 0;
        for (Pesquisa pesquisa : getTodasPesquisas()) {
            if (pesquisa.getTipoPesquisa() == TipoPesquisa.CSAT && pesquisa.getNota() == 1) contador++;
        }
        return contador;
    }
    public int getNota2CSAT(){
        int contador = 0;
        for (Pesquisa pesquisa : getTodasPesquisas()) {
            if (pesquisa.getTipoPesquisa() == TipoPesquisa.CSAT && pesquisa.getNota() == 2) contador++;
        }
        return contador;
    }
    public int getNota3CSAT(){
        int contador = 0;
        for (Pesquisa pesquisa : getTodasPesquisas()) {
            if (pesquisa.getTipoPesquisa() == TipoPesquisa.CSAT && pesquisa.getNota() == 3) contador++;
        }
        return contador;
    }
    public int getNota4CSAT(){
        int contador = 0;
        for (Pesquisa pesquisa : getTodasPesquisas()) {
            if (pesquisa.getTipoPesquisa() == TipoPesquisa.CSAT && pesquisa.getNota() == 4) contador++;
        }
        return contador;
    }
    public int getNota5CSAT(){
        int contador = 0;
        for (Pesquisa pesquisa : getTodasPesquisas()) {
            if (pesquisa.getTipoPesquisa() == TipoPesquisa.CSAT && pesquisa.getNota() == 5) contador++;
        }
        return contador;
    }

    public double getMediaNotasCSAT(){
        int soma = 0;
        int quantidade = 0;

        for (Pesquisa pesquisa : getTodasPesquisas()) {
            if (pesquisa.getTipoPesquisa() == TipoPesquisa.CSAT) {
                soma += pesquisa.getNota();
                quantidade++;
            }
        }
        if (quantidade == 0) {
            return 0.0;
        }
        return (double) soma / quantidade;
    }
    public int getPorcentagemRespostasCSAT(){
        if(getTodasPesquisas().isEmpty()) return 0;
        int contador = 0;
        int total = 0;
        for (Pesquisa pesquisa : getTodasPesquisas()) {
            if(pesquisa.getTipoPesquisa() == TipoPesquisa.CSAT){
                total++;
                if(pesquisa.getResposta() != null){
                    contador++;
                }
            }
        }
        if (total == 0) return 0;
        return contador * 100 / total;
    }
}
