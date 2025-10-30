package com.cesarschool.catalisabackend.models.pergunta;

import com.cesarschool.catalisabackend.models.pergunta.Pergunta;
import com.cesarschool.catalisabackend.models.pergunta.PerguntaRequestDTO;
import com.cesarschool.catalisabackend.models.pergunta.PerguntaResponseDTO;
import com.cesarschool.catalisabackend.models.pergunta.PerguntaService;
import com.cesarschool.catalisabackend.models.utils.ResultService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * API REST para Pergunta
 * Endpoints:
 *  - POST   /api/v1/perguntas
 *  - GET    /api/v1/perguntas
 *  - GET    /api/v1/perguntas/{id}
 *  - GET    /api/v1/perguntas/by-texto/{texto}
 *  - PUT    /api/v1/perguntas/{id}
 *  - PUT    /api/v1/perguntas/by-texto/{textoAlvo}
 *  - DELETE /api/v1/perguntas/{id}
 *  - DELETE /api/v1/perguntas/by-texto/{texto}
 */
@CrossOrigin(origins = "*") // ajuste em produção
@RestController
@RequestMapping("/api/v1/perguntas")
public class PerguntaController {

    private final PerguntaService service;

    public PerguntaController(PerguntaService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody PerguntaRequestDTO req) {
        Pergunta toSave = new Pergunta(req.texto(), req.notaMinima(), req.notaMaxima(), req.tipo());

        ResultService result = service.include(toSave);
        if (!result.isValid()) {
            return badRequest(result);
        }
        if (!result.isRealized()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError(409, "Conflict", result));
        }

        Pergunta saved = service.search(req.texto());
        PerguntaResponseDTO body = PerguntaResponseDTO.fromEntity(saved);
        return ResponseEntity.created(URI.create("/api/v1/perguntas/" + body.id())).body(body);
    }

    // LIST
    @GetMapping
    public List<PerguntaResponseDTO> listAll() {
        return service.findAll().stream()
                .map(PerguntaResponseDTO::fromEntity)
                .toList();
    }

    // READ by id
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable long id) {
        Pergunta p = service.search(id);
        if (p == null) return notFound("Pergunta não encontrada");
        return ResponseEntity.ok(PerguntaResponseDTO.fromEntity(p));
    }

    // READ by texto (exato)
    @GetMapping("/by-texto/{texto}")
    public ResponseEntity<?> findByTexto(@PathVariable String texto) {
        Pergunta p = service.search(texto);
        if (p == null) return notFound("Pergunta não encontrada");
        return ResponseEntity.ok(PerguntaResponseDTO.fromEntity(p));
    }

    // UPDATE por ID (PUT completo)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable long id, @Valid @RequestBody PerguntaRequestDTO req) {
        Pergunta dados = new Pergunta(req.texto(), req.notaMinima(), req.notaMaxima(), req.tipo());
        ResultService result = service.updateById(id, dados);
        if (!result.isValid()) {
            return badRequest(result);
        }
        if (!result.isRealized()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError(409, "Conflict", result));
        }
        Pergunta updated = service.search(id);
        return ResponseEntity.ok(PerguntaResponseDTO.fromEntity(updated));
    }

    // UPDATE por TEXTO-ALVO (PUT completo)
    @PutMapping("/by-texto/{textoAlvo}")
    public ResponseEntity<?> updateByTexto(@PathVariable String textoAlvo, @Valid @RequestBody PerguntaRequestDTO req) {
        Pergunta dados = new Pergunta(req.texto(), req.notaMinima(), req.notaMaxima(), req.tipo());
        ResultService result = service.updateByTexto(textoAlvo, dados);
        if (!result.isValid()) {
            return badRequest(result);
        }
        if (!result.isRealized()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError(409, "Conflict", result));
        }
        // se o texto foi alterado, busque pelo novo; senão, pelo alvo
        Pergunta updated = (req.texto() != null && !req.texto().equalsIgnoreCase(textoAlvo))
                ? service.search(req.texto())
                : service.search(textoAlvo);
        return ResponseEntity.ok(PerguntaResponseDTO.fromEntity(updated));
    }

    // DELETE por id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable long id) {
        ResultService result = service.delete(id);
        if (!result.isValid()) {
            return badRequest(result);
        }
        if (!result.isRealized()) {
            return notFound(result);
        }
        return ResponseEntity.noContent().build();
    }

    // DELETE por texto
    @DeleteMapping("/by-texto/{texto}")
    public ResponseEntity<?> deleteByTexto(@PathVariable String texto) {
        ResultService result = service.delete(texto);
        if (!result.isValid()) {
            return badRequest(result);
        }
        if (!result.isRealized()) {
            return notFound(result);
        }
        return ResponseEntity.noContent().build();
    }

    // ---------- Helpers de erro padronizado ----------

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
        String message;
        try {
            // Ajuste o getter conforme sua classe ResultService (getErros/getError)
            message = (result.getError() != null) ? result.getError().toString() : error;
        } catch (Exception e) {
            message = error;
        }
        return new ApiError(status, error, message);
    }

    public record ApiError(int status, String error, String message) {}
}