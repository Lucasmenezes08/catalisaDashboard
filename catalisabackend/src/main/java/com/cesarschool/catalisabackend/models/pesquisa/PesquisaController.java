package com.cesarschool.catalisabackend.models.pesquisa;

import com.cesarschool.catalisabackend.models.utils.ResultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.NoSuchElementException;

/**
 * API REST para Pesquisa – pronta para o front consumir.
 *
 * Base URL: /api/v1/pesquisas
 *
 * Endpoints:
 *  - POST   /api/v1/pesquisas
 *  - GET    /api/v1/pesquisas
 *  - GET    /api/v1/pesquisas/{id}
 *  - PUT    /api/v1/pesquisas/{id}
 *  - DELETE /api/v1/pesquisas/{id}
 *
 * Filtros opcionais no GET paginado:
 *  - /api/v1/pesquisas?consumoId={id}
 *  - /api/v1/pesquisas?perguntaId={id}
 *  - /api/v1/pesquisas?respostaId={id}
 *
 * DTOs aceitos/retornados:
 *  - PesquisaRequestDTO  : { "consumo": {...}, "perguntas": [{...}], "respostas": [{...}] }
 *  - PesquisaResponseDTO : { "id", "consumo": {...}, "perguntas": [...], "respostas": [...] }
 *
 * Regras/fluxo:
 *  - Validação de campos delegada ao PesquisaService.validate().
 *  - include() cria; update(id, body) atualiza com base no id do path.
 *  - Exceções e códigos HTTP padronizados:
 *      - 201 Created (POST ok) + Location
 *      - 200 OK (GET/PUT ok)
 *      - 204 No Content (DELETE ok)
 *      - 400 Bad Request (erros de validação do serviço)
 *      - 404 Not Found (recurso inexistente)
 *      - 409 Conflict (constraint/unique/violação regra)
 *      - 422 Unprocessable Entity (@Valid)
 *
 * Exemplos:
 *  - Criar:
 *    POST /api/v1/pesquisas
 *    {
 *      "consumo": { "id": 1 },
 *      "perguntas": [ { "id": 10 }, { "id": 11 } ],
 *      "respostas": [ { "id": 100 } ]
 *    }
 *    -> 201 Created, body com PesquisaResponseDTO
 *
 *  - Listar paginado/filtrado:
 *    GET /api/v1/pesquisas?page=0&size=10&sort=id,desc
 *    GET /api/v1/pesquisas?consumoId=1
 *    GET /api/v1/pesquisas?perguntaId=10
 *    GET /api/v1/pesquisas?respostaId=100
 */
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/pesquisas")
public class PesquisaController {

    private final PesquisaService service;
    private final PesquisaRepository repository;

    // =========================== CREATE ===========================

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody PesquisaRequestDTO body) {
        // Monta a entidade a partir do DTO (seus DTOs trazem entidades completas)
        Pesquisa entity = new Pesquisa();
        entity.setConsumo(body.consumo());
        entity.setPerguntas(body.perguntas());
        entity.setRespostas(body.respostas());

        ResultService result = service.include(entity);
        if (!result.isValid()) {
            return badRequest(result);
        }
        if (!result.isRealized()) {
            // Regra de negócio barrou (ex.: "Pesquisa já existente")
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError("CONFLICT", result));
        }

        // Após salvar, o entity já deve ter ID
        PesquisaResponseDTO dto = PesquisaResponseDTO.fromEntity(entity);
        return ResponseEntity.created(URI.create("/api/v1/pesquisas/" + dto.id())).body(dto);
    }

    // ============================= READ ===========================

    @GetMapping("/{id}")
    public ResponseEntity<PesquisaResponseDTO> findById(@PathVariable Long id) {
        Pesquisa p = service.search(id);
        if (p == null) throw new NoSuchElementException("Pesquisa not found");
        return ResponseEntity.ok(PesquisaResponseDTO.fromEntity(p));
    }

    /**
     * GET paginado com filtros opcionais:
     * - /api/v1/pesquisas
     * - /api/v1/pesquisas?consumoId=1
     * - /api/v1/pesquisas?perguntaId=10
     * - /api/v1/pesquisas?respostaId=100
     */
    @GetMapping
    public ResponseEntity<Page<PesquisaResponseDTO>> list(Pageable pageable,
                                                          @RequestParam(required = false) Long consumoId,
                                                          @RequestParam(required = false) Long perguntaId,
                                                          @RequestParam(required = false) Long respostaId) {

        Page<PesquisaResponseDTO> page;

        if (consumoId != null) {
            page = repository.findByConsumo_Id(consumoId, pageable)
                    .map(PesquisaResponseDTO::fromEntity);

        } else if (perguntaId != null) {
            page = repository.findByPerguntas_Id(perguntaId, pageable)
                    .map(PesquisaResponseDTO::fromEntity);

        } else if (respostaId != null) {
            page = repository.findByRespostas_Id(respostaId, pageable)
                    .map(PesquisaResponseDTO::fromEntity);

        } else {
            page = repository.findAll(pageable)
                    .map(PesquisaResponseDTO::fromEntity);
        }

        return ResponseEntity.ok(page);
    }

    // ============================ UPDATE ==========================

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @Valid @RequestBody PesquisaRequestDTO body) {

        Pesquisa dados = new Pesquisa();
        dados.setConsumo(body.consumo());
        dados.setPerguntas(body.perguntas());
        dados.setRespostas(body.respostas());

        ResultService result = service.update(id, dados);
        if (!result.isValid()) {
            return badRequest(result);
        }
        if (!result.isRealized()) {
            // Service sinalizou que não atualizou (ex.: "Pesquisa inexistente")
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError("NOT_FOUND", result));
        }

        Pesquisa atual = service.search(id);
        return ResponseEntity.ok(PesquisaResponseDTO.fromEntity(atual));
    }

    // ============================ DELETE ==========================

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        ResultService result = service.delete(id);


        if (result.getError() != null && result.getError().toString().contains("Pesquisa inexistente")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError("NOT_FOUND", result));
        }

        if (!result.isValid()) {
            return badRequest(result);
        }
        if (!result.isRealized()) {
            // fallback: não realizou por alguma outra razão
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError("CONFLICT", result));
        }

        return ResponseEntity.noContent().build();
    }

    // ====================== ERROR HANDLERS ========================

    private ResponseEntity<ApiError> badRequest(ResultService result) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiError("BAD_REQUEST", messageFrom(result)));
    }

    private ApiError apiError(String code, ResultService result) {
        return new ApiError(code, messageFrom(result));
    }

    private String messageFrom(ResultService result) {
        return (result != null && result.getError() != null)
                ? result.getError().toString()
                : "Unexpected error";
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiError> handleNotFound(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError("NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            BindException.class
    })
    public ResponseEntity<ApiError> handleValidation(Exception ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ApiError("VALIDATION_ERROR", ex.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleConstraint(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiError("CONSTRAINT_VIOLATION", "Unique constraint or FK violation."));
    }

    public record ApiError(String code, String message) {}
}
