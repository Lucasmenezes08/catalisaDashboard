package com.cesarschool.catalisabackend.models.dashboard;

import com.cesarschool.catalisabackend.models.pesquisa.Pesquisa;
import com.cesarschool.catalisabackend.models.pesquisa.TipoPesquisa;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v2/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    private final DashboardService dashboardService;
    private final AnaliseSentimentoService analiseSentimentoService;

    public DashboardController(DashboardService dashboardService,  AnaliseSentimentoService analiseSentimentoService) {
        this.dashboardService = dashboardService;
        this.analiseSentimentoService = analiseSentimentoService;
    }

    // ===================== NPS =====================

    // Retorna apenas o valor do NPS (ex: 59)
    @GetMapping("/nps")
    public ResponseEntity<?> getNPS() {
        int nps = dashboardService.getNPS();
        return ResponseEntity.ok(nps);
    }

    // Distribuição absoluta de NPS (quantidade de clientes)
    // { "promotores": 710, "neutros": 174, "detratores": 116 }
    @GetMapping("/nps/distribuicao")
    public ResponseEntity<?> getDistribuicaoNPS() {
        Map<String, Integer> body = new HashMap<>();
        body.put("promotores", dashboardService.getNumeroPromotoresNPS());
        body.put("neutros", dashboardService.getNumeroNeutrosNPS());
        body.put("detratores", dashboardService.getNumeroDetratoresNPS());
        return ResponseEntity.ok(body);
    }

    // Proporção em % da distribuição NPS
    // { "promotores": 71, "neutros": 17, "detratores": 12 }
    @GetMapping("/nps/proporcao")
    public ResponseEntity<?> getProporcaoNPS() {
        Map<String, Integer> body = new HashMap<>();
        body.put("promotores", dashboardService.getPorcentagemNumeroPromotoresNPS());
        body.put("neutros", dashboardService.getPorcentagemNumeroNeutrosNPS());
        body.put("detratores", dashboardService.getPorcentagemNumeroDetratoresNPS());
        return ResponseEntity.ok(body);
    }

    // Taxa de respostas NPS em %
    // { "porcentagemRespostas": 20 }
    @GetMapping("/nps/respostas")
    public ResponseEntity<?> getTaxaRespostasNPS() {
        Map<String, Integer> body = new HashMap<>();
        body.put("porcentagemRespostas", dashboardService.getPorcentagemRespostasNPS());
        return ResponseEntity.ok(body);
    }

    @GetMapping("/nps/sentimento")
    public ResponseEntity<?> getSentimentoNPS() {
        Map<String, Object> resultado = dashboardService.calcularSentimentoNps();
        return ResponseEntity.ok(resultado);
    }
    @GetMapping("/nps/sentimento/{id}")
    public ResponseEntity<?> getSentimentoIdNPS(@PathVariable("id") Long id) {
        Pesquisa pesquisa = dashboardService.getPesquisaRepository()
                .findById(id)
                .orElse(null);

        if (pesquisa == null) {
            return ResponseEntity.notFound().build();
        }

        if (pesquisa.getTipoPesquisa() != TipoPesquisa.NPS) {
            return ResponseEntity.badRequest().body("A pesquisa informada não é do tipo NPS.");
        }

        if (pesquisa.getResposta() == null || pesquisa.getResposta().isBlank()) {
            Map<String, Object> body = new HashMap<>();
            body.put("id", pesquisa.getId());
            body.put("sentimento", null);
            return ResponseEntity.ok(body);
        }
        Sentimento sentimento = analiseSentimentoService.classificar(pesquisa.getResposta());

        Map<String, Object> body = new HashMap<>();
        body.put("id", pesquisa.getId());
        body.put("sentimento", sentimento.getDescricao());

        return ResponseEntity.ok(body);
    }

    // ===================== CSAT =====================

    // Distribuição das notas de CSAT
    // {
    //   "nota1": 10,
    //   "nota2": 5,
    //   "nota3": 20,
    //   "nota4": 40,
    //   "nota5": 25
    // }
    @GetMapping("/csat/distribuicao")
    public ResponseEntity<?> getDistribuicaoCSAT() {
        Map<String, Integer> body = new HashMap<>();
        body.put("nota0", dashboardService.getNota0CSAT());
        body.put("nota1", dashboardService.getNota1CSAT());
        body.put("nota2", dashboardService.getNota2CSAT());
        body.put("nota3", dashboardService.getNota3CSAT());
        body.put("nota4", dashboardService.getNota4CSAT());
        body.put("nota5", dashboardService.getNota5CSAT());
        return ResponseEntity.ok(body);
    }

    // Média das notas de CSAT
    // { "media": 3.34 }
    @GetMapping("/csat/media")
    public ResponseEntity<?> getMediaNotasCSAT() {
        Map<String, Double> body = new HashMap<>();
        body.put("media", dashboardService.getMediaNotasCSAT());
        return ResponseEntity.ok(body);
    }

    // Taxa de respostas CSAT em %
    // { "porcentagemRespostas": 35 }
    @GetMapping("/csat/respostas")
    public ResponseEntity<?> getTaxaRespostasCSAT() {
        Map<String, Integer> body = new HashMap<>();
        body.put("porcentagemRespostas", dashboardService.getPorcentagemRespostasCSAT());
        return ResponseEntity.ok(body);
    }
    @GetMapping("/csat/sentimento")
    public ResponseEntity<?> getSentimentoCSAT() {
        Map<String, Object> resultado = dashboardService.calcularSentimentoCsat();
        return ResponseEntity.ok(resultado);
    }
    @GetMapping("/csat/sentimento/{id}")
    public ResponseEntity<?> getSentimentoIdCSAT(@PathVariable("id") Long id) {
        Pesquisa pesquisa = dashboardService.getPesquisaRepository()
                .findById(id)
                .orElse(null);

        if (pesquisa == null) {
            return ResponseEntity.notFound().build();
        }

        if (pesquisa.getTipoPesquisa() != TipoPesquisa.CSAT) {
            return ResponseEntity.badRequest().body("A pesquisa informada não é do tipo CSAT.");
        }

        if (pesquisa.getResposta() == null || pesquisa.getResposta().isBlank()) {
            Map<String, Object> body = new HashMap<>();
            body.put("id", pesquisa.getId());
            body.put("sentimento", null);
            return ResponseEntity.ok(body);
        }

        Sentimento sentimento = analiseSentimentoService.classificar(pesquisa.getResposta());

        Map<String, Object> body = new HashMap<>();
        body.put("id", pesquisa.getId());
        body.put("sentimento", sentimento.getDescricao());

        return ResponseEntity.ok(body);
    }

}
