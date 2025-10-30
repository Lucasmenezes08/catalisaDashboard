package com.cesarschool.catalisabackend.models.product;

import com.cesarschool.catalisabackend.models.utils.ResultService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * API REST para Product, pronta para o front consumir.
 * Endpoints:
 *  - POST   /api/v1/products
 *  - GET    /api/v1/products
 *  - GET    /api/v1/products/{id}
 *  - GET    /api/v1/products/by-name/{name}
 *  - PUT    /api/v1/products/{id}
 *  - DELETE /api/v1/products/{id}
 *  - DELETE /api/v1/products/by-name/{name}
 */
@CrossOrigin(origins = "*") // ajuste para o domínio/porta do front em produção
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService productService) {
        this.service = productService;
    }
    // CREATE: POST /api/v1/products
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ProductRequestDTO req) {
        // monta a entidade a partir do DTO de entrada
        Product toSave = new Product(req.name(), req.type(), req.description());

        ResultService result = service.include(toSave);
        if (!result.isValid()) {
            return badRequest(result);
        }
        if (!result.isRealized()) {
            // Regra de negócio impediu (ex.: "Produto já existe")
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError(409, "Conflict", result));
        }

        // Buscar o que acabou de ser salvo para devolver ao cliente
        Product saved = service.search(req.name());
        ProductResponseDTO body = ProductResponseDTO.fromEntity(saved);
        // location opcional, apontando para o recurso criado
        return ResponseEntity.created(URI.create("/api/v1/products/" + body.id())).body(body);
    }

    // LIST: GET /api/v1/products
    @GetMapping
    public List<ProductResponseDTO> listAll() {
        return service.findAll()
                .stream()
                .map(ProductResponseDTO::fromEntity)
                .toList();
    }

    // READ by id: GET /api/v1/products/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable long id) {
        Product p = service.search(id);
        if (p == null) return notFound("Produto não encontrado");
        return ResponseEntity.ok(ProductResponseDTO.fromEntity(p));
    }

    // READ by name (exato): GET /api/v1/products/by-name/{name}
    @GetMapping("/by-name/{name}")
    public ResponseEntity<?> findByName(@PathVariable String name) {
        Product p = service.search(name);
        if (p == null) return notFound("Produto não encontrado");
        return ResponseEntity.ok(ProductResponseDTO.fromEntity(p));
    }

    // UPDATE (PUT completo): PUT /api/v1/products/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id, @Valid @RequestBody ProductRequestDTO req) {
        Product existing = service.search(id);
        if (existing == null) return notFound("Produto inexistente");

        existing.setName(req.name());
        existing.setType(req.type());
        existing.setDescription(req.description());

        ResultService result = service.updateById(existing.getId(),existing);
        if (!result.isValid()) {
            return badRequest(result);
        }
        if (!result.isRealized()) {
            // Por exemplo, conflito de nome único ou outra regra
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError(409, "Conflict", result));
        }

        // Após update, devolve o estado atual
        Product updated = service.search(id);
        return ResponseEntity.ok(ProductResponseDTO.fromEntity(updated));
    }

    // DELETE by id: DELETE /api/v1/products/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable long id) {
        ResultService result = service.delete(id);
        if (!result.isValid()) {
            return badRequest(result);
        }
        if (!result.isRealized()) {
            // Ex.: "ID inexistente"
            return notFound(result);
        }
        return ResponseEntity.noContent().build();
    }

    // DELETE by name: DELETE /api/v1/products/by-name/{name}
    @DeleteMapping("/by-name/{name}")
    public ResponseEntity<?> deleteByName(@PathVariable String name) {
        ResultService result = service.delete(name);
        if (!result.isValid()) {
            return badRequest(result);
        }
        if (!result.isRealized()) {
            // Ex.: "Nome inexistente"
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
        // Tenta extrair mensagens de ListaString
        String message = result.getError() != null ? result.getError().toString() : error;
        return new ApiError(status, error, message);
    }

    // DTO simples de erro (poderia evoluir para trazer fieldErrors, timestamp, etc.)
    public record ApiError(int status, String error, String message) {}
}