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
 *  - POST   /api/v2/products
 *  - GET    /api/v2/products
 *  - GET    /api/v2/products/{id}
 *  - GET    /api/v2/products/by-name/{name}
 *  - PUT    /api/v2/products/{id}
 *  - DELETE /api/v2/products/{id}
 *  - DELETE /api/v2/products/by-name/{name}
 */
@CrossOrigin(origins = "*") // ajuste para o domínio/porta do front em produção
@RestController
@RequestMapping("/api/v2/products")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService productService) {
        this.service = productService;
    }

    // CREATE: POST /api/v2/products
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ProductRequestDTO req) {
        Product toSave = new Product(req.name(), req.type(), req.description());

        ResultService result = service.include(toSave);
        if (!result.isValid()) {
            return badRequest(result);
        }
        if (!result.isRealized()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError(409, "Conflict", result));
        }

        Product saved = service.search(req.name());
        ProductResponseDTO body = ProductResponseDTO.fromEntity(saved);
        return ResponseEntity.created(URI.create("/api/v2/products/" + body.id())).body(body);
    }

    // LIST: GET /api/v2/products
    @GetMapping
    public List<ProductResponseDTO> listAll() {
        return service.findAll()
                .stream()
                .map(ProductResponseDTO::fromEntity)
                .toList();
    }

    // READ by id: GET /api/v2/products/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable long id) {
        Product p = service.search(id);
        if (p == null) return notFound("Produto não encontrado");
        return ResponseEntity.ok(ProductResponseDTO.fromEntity(p));
    }

    // READ by name (exato): GET /api/v2/products/by-name/{name}
    @GetMapping("/by-name/{name}")
    public ResponseEntity<?> findByName(@PathVariable String name) {
        Product p = service.search(name);
        if (p == null) return notFound("Produto não encontrado");
        return ResponseEntity.ok(ProductResponseDTO.fromEntity(p));
    }

    // UPDATE (PUT completo): PUT /api/v2/products/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id, @Valid @RequestBody ProductRequestDTO req) {
        Product existing = service.search(id);
        if (existing == null) return notFound("Produto inexistente");

        existing.setName(req.name());
        existing.setType(req.type());
        existing.setDescription(req.description());

        ResultService result = service.updateById(existing.getId(), existing);
        if (!result.isValid()) {
            return badRequest(result);
        }
        if (!result.isRealized()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError(409, "Conflict", result));
        }

        Product updated = service.search(id);
        return ResponseEntity.ok(ProductResponseDTO.fromEntity(updated));
    }

    // DELETE by id: DELETE /api/v2/products/{id}
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

    // DELETE by name: DELETE /api/v2/products/by-name/{name}
    @DeleteMapping("/by-name/{name}")
    public ResponseEntity<?> deleteByName(@PathVariable String name) {
        ResultService result = service.delete(name);
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
        String message = result.getError() != null ? result.getError().toString() : error;
        return new ApiError(status, error, message);
    }

    public record ApiError(int status, String error, String message) {}
}
