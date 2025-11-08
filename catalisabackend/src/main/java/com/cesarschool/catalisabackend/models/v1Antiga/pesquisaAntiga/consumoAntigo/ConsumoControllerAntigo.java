//package com.cesarschool.catalisabackend.models.v1Antiga.pesquisaAntiga.consumoAntigo;
//
//import com.cesarschool.catalisabackend.models.v1Antiga.pesquisaAntiga.PesquisaAntiga;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import com.cesarschool.catalisabackend.models.utils.ResultService;
//
//import java.net.URI;
//import java.time.LocalDate;
//import java.util.NoSuchElementException;
//
///**
// * API REST para Consumo (User consumindo Product) — pronta para o front.
// *
// * Base path: /api/v1/consumos
// *
// * Endpoints:
// *  - POST   /api/v1/consumos
// *  - GET    /api/v1/consumos
// *  - GET    /api/v1/consumos/{id}
// *  - PATCH  /api/v1/consumos/{id}
// *  - DELETE /api/v1/consumos/{id}
// *
// * Filtros opcionais no GET paginado (/api/v1/consumos):
// *  - ?userId={id}                 -> consumos de um usuário
// *  - ?productId={id}              -> consumos de um produto
// *  - ?respondida={true|false}     -> se pesquisa foi respondida
// *  - ?startDate=YYYY-MM-DD        -> data inicial (inclusiva)
// *  - ?endDate=YYYY-MM-DD          -> data final   (inclusiva)
// *  - Com paginação: page, size, sort (ex.: sort=dataConsumo,desc)
// *
// * Contratos:
// *  - ConsumoRequestDTO  : { user, product, dataConsumo, avaliacao, pesquisaRespondida, pesquisa }
// *    *IMPORTANTE*: o seu DTO recebe objetos completos (User/Product/Pesquisa).
// *                  Em produção normalmente receberíamos apenas IDs, mas aqui seguimos seu modelo.
// *
// *  - ConsumoResponseDTO : { id, user, product, dataConsumo, avaliacao, pesquisaRespondida, pesquisa }
// *
// * Regras / Observações:
// *  - create/update passam pelo ConsumoService.validate().
// *  - Em caso de erro de validação, retornamos 422 com lista de mensagens.
// *  - Em caso de recurso não encontrado, retornamos 404.
// *  - Em caso de conflito/estado inválido, retornamos 400.
// *
// * Exemplos:
// *  - Criar:
// *      POST /api/v1/consumos
// *      {
// *        "user": { ... },
// *        "product": { ... },
// *        "dataConsumo": "2025-11-04",
// *        "avaliacao": 5,
// *        "pesquisaRespondida": true,
// *        "pesquisa": { ... }
// *      }
// *      -> 201 Created + Location: /api/v1/consumos/{id}
// *
// *  - Listar com filtros:
// *      GET /api/v1/consumos?userId=10&startDate=2025-10-01&endDate=2025-11-30&page=0&size=10&sort=dataConsumo,desc
// */
//@RestController
//@RequiredArgsConstructor
//@CrossOrigin(origins = "*")
//@RequestMapping("/api/v1/consumos")
//public class ConsumoControllerAntigo {
//
//    private final ConsumoServiceAntigo consumoServiceAntigo;
//    private final ConsumoRepositoryAntigo consumoRepositoryAntigo;
//
//    // ------------------------ CREATE ------------------------
//
//    @PostMapping
//    public ResponseEntity<?> create(@Valid @RequestBody ConsumoRequestDTOAntigo body) {
//        ConsumoAntigo entity = toEntity(body, null);
//        var result = consumoServiceAntigo.include(entity);
//
//        if (!result.isValid()) {
//            return unprocessable(result);
//        }
//        if (!result.isRealized()) {
//            return badRequest(result);
//        }
//
//        // entity persistido — buscar o ID recém-gerado
//        // Como o service salva a mesma instância, o ID já estará no entity
//        ConsumoResponseDTOAntigo dto = toResponse(entity);
//        return ResponseEntity
//                .created(URI.create("/api/v1/consumos/" + dto.id()))
//                .body(dto);
//    }
//
//    // ------------------------ READ (LIST) -------------------
//
//    @GetMapping
//    public ResponseEntity<Page<ConsumoResponseDTOAntigo>> list(
//            Pageable pageable,
//            @RequestParam(required = false) Long userId,
//            @RequestParam(required = false) Long productId,
//            @RequestParam(required = false) Boolean respondida,
//            @RequestParam(required = false) LocalDate startDate,
//            @RequestParam(required = false) LocalDate endDate
//    ) {
//        Page<ConsumoAntigo> page;
//
//        // Prioridade de filtros combináveis: userId/productId + data/flag
//        if (userId != null && productId != null) {
//            page = consumoRepositoryAntigo.findByUser_IdAndProduct_Id(userId, productId, pageable);
//        } else if (userId != null) {
//            page = consumoRepositoryAntigo.findByUser_Id(userId, pageable);
//        } else if (productId != null) {
//            page = consumoRepositoryAntigo.findByProduct_Id(productId, pageable);
//        } else if (respondida != null) {
//            page = consumoRepositoryAntigo.findByPesquisaRespondida(respondida, pageable);
//        } else if (startDate != null && endDate != null) {
//            page = consumoRepositoryAntigo.findByDataConsumoBetween(startDate, endDate, pageable);
//        } else {
//            page = consumoRepositoryAntigo.findAll(pageable);
//        }
//
//        return ResponseEntity.ok(page.map(this::toResponse));
//    }
//
//    // ------------------------ READ (BY ID) ------------------
//
//    @GetMapping("/{id}")
//    public ResponseEntity<ConsumoResponseDTOAntigo> getById(@PathVariable Long id) {
//        ConsumoAntigo entity = consumoRepositoryAntigo.findById(id)
//                .orElseThrow(() -> new NoSuchElementException("Consumo não encontrado"));
//        return ResponseEntity.ok(toResponse(entity));
//    }
//
//    // ------------------------ UPDATE -----------------------
//
//    @PatchMapping("/{id}")
//    public ResponseEntity<?> update(@PathVariable Long id,
//                                    @Valid @RequestBody ConsumoRequestDTOAntigo body) {
//        ConsumoAntigo exists = consumoRepositoryAntigo.findById(id)
//                .orElseThrow(() -> new NoSuchElementException("Consumo não encontrado"));
//
//        ConsumoAntigo toUpdate = toEntity(body, exists); // reaproveita a instância existente
//        var result = consumoServiceAntigo.update(id, toUpdate);
//
//        if (!result.isValid()) {
//            return unprocessable(result);
//        }
//        if (!result.isRealized()) {
//            return badRequest(result);
//        }
//        return ResponseEntity.ok(toResponse(exists));
//    }
//
//    // ------------------------ DELETE -----------------------
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> delete(@PathVariable Long id) {
//        ConsumoAntigo exists = consumoRepositoryAntigo.findById(id)
//                .orElseThrow(() -> new NoSuchElementException("Consumo não encontrado"));
//        var result = consumoServiceAntigo.delete(id);
//
//        if (!result.isValid()) {
//            // Erro lógico — por simplicidade, tratamos como 400
//            return ResponseEntity.badRequest().build();
//        }
//        return ResponseEntity.noContent().build();
//    }
//
//    // ------------------------ MAPEADORES --------------------
//
//    @PersistenceContext
//    private EntityManager em;
//
//    private ConsumoAntigo toEntity(ConsumoRequestDTOAntigo dto, ConsumoAntigo targetOrNull) {
//        ConsumoAntigo c = (targetOrNull != null) ? targetOrNull : new ConsumoAntigo();
//
//        if (dto.userId() != null) {
//            c.setUser(em.getReference(com.cesarschool.catalisabackend.models.user.User.class, dto.userId()));
//        }
//        if (dto.productId() != null) {
//            c.setProduct(em.getReference(com.cesarschool.catalisabackend.models.product.Product.class, dto.productId()));
//        }
//        c.setDataConsumo(dto.dataConsumo());
//        c.setAvaliacao(dto.avaliacao() == null ? 0 : dto.avaliacao());
//        c.setPesquisaRespondida(Boolean.TRUE.equals(dto.pesquisaRespondida()));
//
//        if (dto.pesquisaId() != null) {
//            c.setPesquisaAntiga(em.getReference(PesquisaAntiga.class, dto.pesquisaId()));
//        }
//
//        return c;
//    }
//
//    private ConsumoResponseDTOAntigo toResponse(ConsumoAntigo c) {
//        return new ConsumoResponseDTOAntigo(
//                c.getId(),
//                c.getUser() != null ? c.getUser().getId() : null,
//                c.getProduct() != null ? c.getProduct().getId() : null,
//                c.getDataConsumo(),
//                c.getAvaliacao(),
//                c.isPesquisaRespondida(),
//                c.getPesquisaAntiga() != null ? c.getPesquisaAntiga().getId() : null
//        );
//    }
//
//    // ------------------------ ERROS PADRÃO ------------------
//
//    private ResponseEntity<ApiError> unprocessable(ResultService r) {
//        String msg = (r != null && r.getError() != null)
//                ? String.join("; ", r.getError().listar())
//                : "Dados inválidos";
//
//        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
//                .body(new ApiError("VALIDATION_ERROR", msg));
//    }
//
//    private ResponseEntity<ApiError> badRequest(ResultService r) {
//        String msg = (r != null && r.getError() != null)
//                ? String.join("; ", r.getError().listar())
//                : "Regra de negócio violada";
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                .body(new ApiError("BUSINESS_RULE", msg));
//    }
//
//    @ExceptionHandler(NoSuchElementException.class)
//    public ResponseEntity<ApiError> handleNotFound(NoSuchElementException ex) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiError("NOT_FOUND", ex.getMessage()));
//    }
//
//    public record ApiError(String code, String message) {}
//}
