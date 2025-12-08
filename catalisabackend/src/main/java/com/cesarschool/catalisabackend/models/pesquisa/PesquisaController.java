package com.cesarschool.catalisabackend.models.pesquisa;

import com.cesarschool.catalisabackend.models.consumo.Consumo;
import com.cesarschool.catalisabackend.models.product.Product;
import com.cesarschool.catalisabackend.models.user.User;
import com.cesarschool.catalisabackend.models.user.UserResponseDTO;
import com.cesarschool.catalisabackend.models.utils.ResultService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v2/pesquisas")
public class PesquisaController {

    private final PesquisaService pesquisaService;
    private final PesquisaRepository pesquisaRepository;

    @PersistenceContext
    private EntityManager em;

    public PesquisaController(PesquisaService pesquisaService, PesquisaRepository pesquisaRepository) {
        this.pesquisaService = pesquisaService;
        this.pesquisaRepository = pesquisaRepository;
    }

    // ========= CREATE =========
    // ========= CREATE =========
    @PostMapping
    @Transactional
    public ResponseEntity<?> criar(@Valid @RequestBody PesquisaRequestDTO dto, BindingResult br) {
        if (br.hasErrors()) return ResponseEntity.badRequest().body(br.getAllErrors());

        Consumo consumo = em.find(Consumo.class, dto.consumoId());
        if (consumo == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Consumo não encontrado: id=" + dto.consumoId());
        }

        final Pesquisa nova;
        try {
            // ⚠️ Pode lançar IllegalArgumentException se nota não estiver no range do tipo
            nova = new Pesquisa(consumo, dto.nota(), dto.dataPesquisa(), dto.tipoPesquisa(), dto.resposta());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // 400 em vez de 500
        }

        ResultService result = pesquisaService.createPesquisa(nova);
        if (!result.isValid())    return ResponseEntity.badRequest().body(result.getError().listar());
        if (!result.isRealized()) return ResponseEntity.status(HttpStatus.CONFLICT).body(result.getError().listar());

        return ResponseEntity.status(HttpStatus.CREATED).body(PesquisaResponseDTO.fromEntity(nova));
    }

    // ========= READ BY ID =========
    @GetMapping("/{id}/produto")
    public ResponseEntity<?> getProduto(@PathVariable("id") long id) {
        Product produto = pesquisaService.getPesquisa(id).getConsumo().getProduto();
        if(produto == null){
            throw new RuntimeException("Pesquisa inexistente");
        }
        Map<String, String> body = new HashMap<>();
        body.put("NomeProduto", produto.getName());
        body.put("descricao", produto.getDescription());
        return ResponseEntity.ok(body);
    }
    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<?> obterPorId(@PathVariable long id) {
        Pesquisa p = pesquisaService.getPesquisa(id);
        if (p == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(PesquisaResponseDTO.fromEntity(p));
    }

    // ========= READ BY CONSUMO =========
    @GetMapping("/by-consumo/{consumoId}")
    @Transactional(readOnly = true)
    public ResponseEntity<?> obterPorConsumo(@PathVariable long consumoId) {
        Consumo consumo = em.find(Consumo.class, consumoId);
        if (consumo == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Consumo não encontrado: id=" + consumoId);
        }
        Pesquisa p = pesquisaService.getPesquisa(consumo);
        if (p == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(PesquisaResponseDTO.fromEntity(p));
    }
    // ============= READ USER ===============
    @GetMapping("/{id}/user")
    public ResponseEntity<?> obterUserDaPesquisa(@PathVariable long id) {
        Pesquisa pesquisa = pesquisaService.getPesquisa(id);
        if (pesquisa == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            User user = pesquisaService.getUser(pesquisa);

            UserResponseDTO dto = new UserResponseDTO(
                    user.getId(),
                    user.getEmail(),
                    user.getUsername()
            );

            return ResponseEntity.ok(dto);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ========= LIST / FILTRO (v2) =========
    // Ex.: GET /api/v2/pesquisas?tipoPesquisa=NPS&tipoCliente=PROMOTOR&inicio=2025-01-01&fim=2025-12-31&page=0&size=20
    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<?> listar(
            @RequestParam TipoPesquisa tipoPesquisa,
            @RequestParam(required = false) TipoCliente tipoCliente,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        // período informado?
        boolean temPeriodo = (inicio != null && fim != null);

        Page<Pesquisa> page;

        if (tipoCliente != null && temPeriodo) {
            // v2: agora funciona com tipoCliente + período
            page = pesquisaRepository.findByTipoPesquisaAndTipoClienteAndDataPesquisaBetween(
                    tipoPesquisa, tipoCliente, inicio, fim, pageable);
        } else if (tipoCliente != null) {
            page = pesquisaRepository.findByTipoPesquisaAndTipoCliente(tipoPesquisa, tipoCliente, pageable);
        } else if (temPeriodo) {
            page = pesquisaRepository.findByTipoPesquisaAndDataPesquisaBetween(tipoPesquisa, inicio, fim, pageable);
        } else {
            page = pesquisaRepository.findByTipoPesquisa(tipoPesquisa, pageable);
        }

        return ResponseEntity.ok(page.map(PesquisaResponseDTO::fromEntity));
    }

    // ========= KPIs =========
    // GET /api/v2/pesquisas/kpis?tipoPesquisa=NPS&inicio=2025-01-01&fim=2025-12-31
    @GetMapping("/kpis")
    @Transactional(readOnly = true)
    public ResponseEntity<?> kpis(
            @RequestParam TipoPesquisa tipoPesquisa,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim
    ) {
        Double media = pesquisaRepository.mediaNotaPorTipoPesquisa(tipoPesquisa, inicio, fim);
        List<PesquisaRepository.TipoClienteCount> dist =
                pesquisaRepository.contarPorTipoClienteDentroDoTipoPesquisa(tipoPesquisa, inicio, fim);

        var dto = new KpiResponseDTO(media, dist);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> atualizar(@PathVariable long id,
                                       @Valid @RequestBody PesquisaRequestDTO dto,
                                       BindingResult br) {
        if (br.hasErrors()) return ResponseEntity.badRequest().body(br.getAllErrors());

        Consumo consumo = em.find(Consumo.class, dto.consumoId());
        if (consumo == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Consumo não encontrado: id=" + dto.consumoId());
        }

        final Pesquisa nova;
        try {
            // ⬇️ protege contra nota/tipo inválidos
            nova = new Pesquisa(consumo, dto.nota(), dto.dataPesquisa(), dto.tipoPesquisa(), dto.resposta());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        ResultService result = pesquisaService.updatePesquisa(id, nova);
        if (!result.isValid())    return ResponseEntity.badRequest().body(result.getError().listar());
        if (!result.isRealized()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pesquisa para atualizar inexistente");

        Pesquisa atualizada = pesquisaService.getPesquisa(id);
        return ResponseEntity.ok(PesquisaResponseDTO.fromEntity(atualizada));
    }

    // ========= DELETE =========
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deletar(@PathVariable long id) {
        ResultService result = pesquisaService.deletePesquisa(id);
        if (!result.isValid()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result.getError().listar());
        return ResponseEntity.noContent().build();
    }

    // ===== DTO de resposta para KPIs =====
    public record KpiResponseDTO(
            Double media,
            List<PesquisaRepository.TipoClienteCount> distribuicao
    ) {}
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
    // === Mais handlers para transformar 500 em respostas claras ===
    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<String> handleConstraint(org.springframework.dao.DataIntegrityViolationException ex) {
        // Mostra a causa real (ex.: NOT NULL, FK, UNIQUE) em 409
        String msg = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Violação de integridade: " + msg);
    }

    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleJson(org.springframework.http.converter.HttpMessageNotReadableException ex) {
        // Ex.: enum inválido, data malformada (yyyy-MM-dd)
        String msg = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();
        return ResponseEntity.badRequest().body("JSON inválido: " + msg);
    }

    @ExceptionHandler(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatch(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.badRequest().body("Parâmetro inválido: " + ex.getName() + " = " + String.valueOf(ex.getValue()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAny(Exception ex) {
        ex.printStackTrace(); // log no backend p/ você ver a stack
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());
    }
}
