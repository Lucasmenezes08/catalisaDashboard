package com.cesarschool.catalisabackend.models.v1Antiga.pesquisaAntiga.resposta;

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
 * API REST para Resposta, pronta para o front consumir.
 *
 * Base URL: /api/v1/respostas
 *
 * Endpoints:
 *  - POST   /api/v1/respostas
 *  - GET    /api/v1/respostas
 *  - GET    /api/v1/respostas/{id}
 *  - PUT    /api/v1/respostas/{id}
 *  - DELETE /api/v1/respostas/{id}
 *
 * Filtros opcionais no GET paginado:
 *  - /api/v1/respostas?pesquisaId={id}
 *  - /api/v1/respostas?perguntaId={id}
 *
 * DTOs:
 *  - RespostaRequestDTO  : { "pesquisa": { "id": ... }, "pergunta": { "id": ... }, "resposta": "..." }
 *  - RespostaResponseDTO : { "id", "pesquisa": { ... }, "pergunta": { ... }, "resposta" }
 *
 * Regras de negócio (delegadas ao RespostaService):
 *  - Valida presença de pesquisa, pergunta e texto de resposta.
 *  - Em include(), hoje valida duplicidade por id (não evita duplicidade por (pesquisa,pergunta)).
 *  - Em update(), atualiza por {id} do path (e usa os dados do body).
 *
 * Exemplos de requisição (POST):
 *  POST /api/v1/respostas
 *  {
 *    "pesquisa": { "id": 1 },
 *    "pergunta": { "id": 10 },
 *    "resposta": "Texto digitado pelo usuário"
 *  }
 *  -> 201 Created + body com RespostaResponseDTO
 *
 * Exemplos de listagem paginada:
 *  GET /api/v1/respostas?page=0&size=10&sort=id,desc
 *  GET /api/v1/respostas?pesquisaId=1
 *  GET /api/v1/respostas?perguntaId=10
 *
 * Códigos de status:
 *  - 201 Created (POST bem-sucedido)
 *  - 200 OK (GET/PUT bem-sucedidos)
 *  - 204 No Content (DELETE bem-sucedido)
 *  - 400 Bad Request (erros de validação do serviço)
 *  - 404 Not Found (recurso não existe)
 *  - 409 Conflict (violação de regra/unique, se o service sinalizar)
 *  - 422 Unprocessable Entity (erros de validação @Valid)
 *
 * Observação:
 *  - Como seus DTOs carregam entidades, o body deve conter pelo menos os IDs de pesquisa e pergunta.
 *    Ex.: "pesquisa": {"id": 1}, "pergunta": {"id": 10}
 */
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/respostas")
public class RespostaController {

    private final RespostaService respostaService;
    private final RespostaRepository respostaRepository;

    // ========================== CREATE ==========================

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody RespostaRequestDTO body) {
        // Monta a entidade diretamente dos DTOs (seus DTOs trazem entidades completas)
        Resposta entity = new Resposta(body.pesquisaAntiga(), body.pergunta(), body.resposta());

        ResultService result = respostaService.include(entity);
        if (!result.isValid()) {
            return badRequest(result);
        }
        if (!result.isRealized()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(apiError("CONFLICT", result));
        }

        // Após salvar no service, o entity já deve ter ID gerado (JPA)
        RespostaResponseDTO dto = RespostaResponseDTO.create(entity);
        return ResponseEntity.created(URI.create("/api/v1/respostas/" + dto.id())).body(dto);
    }

    // ============================ READ ==========================

    @GetMapping("/{id}")
    public ResponseEntity<RespostaResponseDTO> findById(@PathVariable Long id) {
        Resposta r = respostaService.search(id);
        if (r == null) {
            throw new NoSuchElementException("Resposta Não encontrada");
        }
        return ResponseEntity.ok(RespostaResponseDTO.create(r));
    }

    /**
     * GET paginado com filtros opcionais:
     * - /api/v1/respostas
     * - /api/v1/respostas?pesquisaId=1
     * - /api/v1/respostas?perguntaId=10
     */
    @GetMapping
    public ResponseEntity<Page<RespostaResponseDTO>> list(Pageable pageable,
                                                          @RequestParam(required = false) Long pesquisaId,
                                                          @RequestParam(required = false) Long perguntaId) {

        Page<RespostaResponseDTO> page;

        if (pesquisaId != null) {
            page = respostaRepository.findByPesquisa_Id(pesquisaId, pageable)
                    .map(RespostaResponseDTO::create);

        } else if (perguntaId != null) {
            page = respostaRepository.findByPergunta_Id(perguntaId, pageable)
                    .map(RespostaResponseDTO::create);

        } else {
            page = respostaRepository.findAll(pageable)
                    .map(RespostaResponseDTO::create);
        }

        return ResponseEntity.ok(page);
    }

    // =========================== UPDATE =========================

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @Valid @RequestBody RespostaRequestDTO body) {

        // Monta um "dados" para o service sobrescrever tudo
        Resposta dados = new Resposta(body.pesquisaAntiga(), body.pergunta(), body.resposta());

        ResultService result = respostaService.update(id, dados);
        if (!result.isValid()) {
            return badRequest(result);
        }
        if (!result.isRealized()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(apiError("NOT_FOUND", result));
        }

        Resposta updated = respostaService.search(id);
        return ResponseEntity.ok(RespostaResponseDTO.create(updated));
    }

    // =========================== DELETE =========================

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        ResultService result = respostaService.delete(id);
        if (!result.isValid()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(apiError("NOT_FOUND", result));
        }
        return ResponseEntity.noContent().build();
    }

    // ====================== ERROR HANDLERS ======================

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

    // DTO simples de erro
    public record ApiError(String code, String message) {}
}
