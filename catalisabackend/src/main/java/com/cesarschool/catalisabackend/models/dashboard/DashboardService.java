package com.cesarschool.catalisabackend.models.dashboard;

import com.cesarschool.catalisabackend.models.pesquisa.Pesquisa;
import com.cesarschool.catalisabackend.models.pesquisa.PesquisaRepository;
import com.cesarschool.catalisabackend.models.pesquisa.TipoCliente;
import com.cesarschool.catalisabackend.models.pesquisa.TipoPesquisa;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Service
public class DashboardService {
    private final PesquisaRepository pesquisaRepository;
    private final AnaliseSentimentoService analiseSentimentoService;

    public DashboardService(PesquisaRepository pesquisaRepository, AnaliseSentimentoService analiseSentimentoService) {
        this.pesquisaRepository = pesquisaRepository;
        this.analiseSentimentoService = analiseSentimentoService;
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

    public int getNota0CSAT() {
        int contador = 0;
        for (Pesquisa pesquisa : getTodasPesquisas()) {
            if (pesquisa.getTipoPesquisa() == TipoPesquisa.CSAT && pesquisa.getNota() == 0) contador++;
        }
        return contador;
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
    public Map<String, Object> calcularSentimentoPorPesquisa(TipoPesquisa tipoPesquisa) {
        List<Pesquisa> pesquisas = pesquisaRepository.findByTipoPesquisa(tipoPesquisa);

        long total = 0;
        long positivos = 0;
        long negativos = 0;

        for (Pesquisa p : pesquisas) {
            String resposta = p.getResposta();

            if (resposta == null || resposta.isBlank()) {
                continue;
            }

            Sentimento s = analiseSentimentoService.classificar(resposta);

            if (s == Sentimento.MUITO_POSITIVO || s == Sentimento.POSITIVO || s == Sentimento.NEUTRO) {
                positivos++;
            } else {
                negativos++;
            }
            total++;
        }
        double percPositivos = 0.0;
        double percNegativos = 0.0;

        if (total > 0) {
            percPositivos = Math.round(percPositivos * 10.0) / 10.0;
            percNegativos = Math.round(percNegativos * 10.0) / 10.0;
        }
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("tipoPesquisa", tipoPesquisa.name());
        resultado.put("totalComentarios", total);
        resultado.put("positivos", positivos);
        resultado.put("negativos", negativos);
        resultado.put("percPositivos", percPositivos);
        resultado.put("percNegativos", percNegativos);

        return resultado;
    }

    public Map<String, Object> calcularSentimentoNps() {
        return calcularSentimentoPorPesquisa(TipoPesquisa.NPS);
    }

    public Map<String, Object> calcularSentimentoCsat() {
        return calcularSentimentoPorPesquisa(TipoPesquisa.CSAT);
    }
}
