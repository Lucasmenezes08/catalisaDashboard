package com.cesarschool.catalisabackend.models.consumo;

import com.cesarschool.catalisabackend.models.utils.ResultService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable; // <- pageable correto
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v2/consumos")
public class ConsumoController {

    private final ConsumoService consumoService;
    private final ConsumoRepository consumoRepository;

    public ConsumoController(ConsumoService consumoService, ConsumoRepository consumoRepository) {
        this.consumoService = consumoService;
        this.consumoRepository = consumoRepository;
    }

    // ===================== CREATE =====================
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ConsumoRequestDTO req) {
        Consumo toSave = new Consumo(
                req.user(),
                req.product(),
                req.consumiuPesquisa(),
                req.pesquisa(),
                req.dataConsumo()
        );

        ResultService result = consumoService.create(toSave);
        if (!result.isValid()) {
            return badRequest(result);
        }
        if (!result.isRealized()) {
            // Ex.: "Consumo ja existente"
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError(409, "Conflict", result));
        }

        // Após salvar, pegamos o "último por usuário" (já que o service cria baseado em user)
        Consumo saved = consumoRepository
                .findTopByUser_IdOrderByDataConsumoDesc(toSave.getUser().getId())
                .orElse(toSave);

        ConsumoResponseDTO body = ConsumoResponseDTO.fromEntity(saved);
        return ResponseEntity.created(URI.create("/api/v2/consumos/" + body.id())).body(body);
    }

    // ===================== READ (LIST) =====================
    // Lista paginada com filtros opcionais: userId, productId, inicio, fim, somenteComPesquisa (true/false)
    @GetMapping
    public ResponseEntity<?> list(
            Pageable pageable,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) LocalDate inicio,
            @RequestParam(required = false) LocalDate fim,
            @RequestParam(required = false) Boolean somenteComPesquisa
    ) {
        Page<Consumo> page;

        // Filtro por "somente com/sem pesquisa" tem prioridade se vier sozinho
        if (somenteComPesquisa != null) {
            if (somenteComPesquisa) {
                page = consumoRepository.findByPesquisaIsNotNull(pageable);
            } else {
                page = consumoRepository.findByPesquisaIsNull(pageable);
            }
        }
        // Filtro por user + intervalo (quando vierem ambos)
        else if (userId != null && inicio != null && fim != null) {
            page = consumoRepository.findByUser_IdAndDataConsumoBetween(userId, inicio, fim, pageable);
        }
        // Filtro somente por user
        else if (userId != null) {
            page = consumoRepository.findByUser_Id(userId, pageable);
        }
        // Filtro somente por product
        else if (productId != null) {
            page = consumoRepository.findByProduto_Id(productId, pageable);
        }
        // Filtro somente por intervalo
        else if (inicio != null && fim != null) {
            page = consumoRepository.findByDataConsumoBetween(inicio, fim, pageable);
        }
        // Sem filtros -> todos
        else {
            page = consumoRepository.findAll(pageable);
        }

        Page<ConsumoResponseDTO> dtoPage = page.map(ConsumoResponseDTO::fromEntity);
        return ResponseEntity.ok(dtoPage);
    }

    // ===================== READ (BY ID) =====================
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        return consumoRepository.findById(id)
                .map(ConsumoResponseDTO::fromEntity)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> notFound("Consumo não encontrado"));
    }

    // ===================== READ (LIST BY USER - sem paginação, lista completa) =====================
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<?> listByUser(@PathVariable Long userId) {
        List<Consumo> list = consumoRepository.findByUser_Id(userId);
        List<ConsumoResponseDTO> body = list.stream().map(ConsumoResponseDTO::fromEntity).toList();
        return ResponseEntity.ok(body);
    }

    // ===================== UPDATE =====================
    // Observação: seu service.update(id, consumo) ignora o id e atualiza pelo "user" (via findByUser(consumo)).
    // Mantive a chamada conforme seu service atual, mas recomendo alinhar a regra para atualizar por ID.
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id, @Valid @RequestBody ConsumoRequestDTO req) {
        Consumo toUpdate = new Consumo(
                req.user(),
                req.product(),
                req.consumiuPesquisa(),
                req.pesquisa(),
                req.dataConsumo()
        );

        ResultService result = consumoService.update(id, toUpdate);
        if (!result.isValid()) {
            return badRequest(result);
        }
        if (!result.isRealized()) {
            return notFound(result); // Ex.: "Consumo nao encontrado ou não existente"
        }

        // Como o service atualiza "pelo user", pegamos o mais recente do user
        Consumo refreshed = consumoRepository
                .findTopByUser_IdOrderByDataConsumoDesc(toUpdate.getUser().getId())
                .orElse(null);

        if (refreshed == null) {
            return notFound("Consumo não encontrado após atualização");
        }
        return ResponseEntity.ok(ConsumoResponseDTO.fromEntity(refreshed));
    }

    // ===================== DELETE =====================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        ResultService result = consumoService.delete(id);
        if (!result.isValid()) {
            return badRequest(result);
        }
        if (!result.isRealized()) {
            return notFound(result);
        }
        return ResponseEntity.noContent().build();
    }

    // ----------------- Helpers de resposta padronizada -----------------
    private ResponseEntity<?> badRequest(ResultService result) {
        return ResponseEntity.badRequest().body(apiError(400, "Bad Request", result));
    }

    private ResponseEntity<?> notFound(String message) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError(404, "Not Found", message));
    }

    private ResponseEntity<?> notFound(ResultService result) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(apiError(404, "Not Found", result));
    }

    private ApiError apiError(int status, String error, ResultService result) {
        String message = (result.getError() != null ? result.getError().toString() : error);
        return new ApiError(status, error, message);
    }

    public record ApiError(int status, String error, String message) {}
}
