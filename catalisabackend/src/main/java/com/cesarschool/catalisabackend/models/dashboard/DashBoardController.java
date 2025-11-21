package com.cesarschool.catalisabackend.models.dashboard;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v2/dashboard")
@CrossOrigin(origins = "*")
public class DashBoardController {

    private final DashboardService dashboardService;

    public DashBoardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
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
}
