package com.cesarschool.catalisabackend.models.consumo;

import com.cesarschool.catalisabackend.models.product.Product;
import com.cesarschool.catalisabackend.models.product.ProductRepository;
import com.cesarschool.catalisabackend.models.user.User;
import com.cesarschool.catalisabackend.models.user.UserRepository;
import com.cesarschool.catalisabackend.models.utils.ResultService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v2/consumos")
public class ConsumoController {

    private final ConsumoService consumoService;
    private final ConsumoRepository consumoRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public ConsumoController(ConsumoService consumoService,
                             ConsumoRepository consumoRepository,
                             UserRepository userRepository,
                             ProductRepository productRepository) {
        this.consumoService = consumoService;
        this.consumoRepository = consumoRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    // ===================== CREATE =====================
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ConsumoRequestDTO req) {
        // Carrega User e Product pelos IDs recebidos
        User user = userRepository.findById(req.userId())
                .orElseThrow(() -> new NoSuchElementException("Usuario não encontrado"));
        Product product = productRepository.findById(req.productId())
                .orElseThrow(() -> new NoSuchElementException("Produto não encontrado"));

        Consumo toSave = new Consumo(
                user,
                product,
                req.consumiuPesquisa(),
                req.pesquisa(),      // se quiser mudar para pesquisaId depois, ajusta aqui também
                req.dataConsumo()
        );

        ResultService result = consumoService.create(toSave);
        if (!result.isValid()) {
            return badRequest(result);
        }
        if (!result.isRealized()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError(409, "Conflict", result));
        }

        Consumo saved = consumoRepository
                .findTopByUser_IdOrderByDataConsumoDesc(user.getId())
                .orElse(toSave);

        ConsumoResponseDTO body = ConsumoResponseDTO.fromEntity(saved);
        return ResponseEntity.created(URI.create("/api/v2/consumos/" + body.id())).body(body);
    }

    // ===================== READ (LIST) =====================
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

        if (somenteComPesquisa != null) {
            page = somenteComPesquisa
                    ? consumoRepository.findByPesquisaIsNotNull(pageable)
                    : consumoRepository.findByPesquisaIsNull(pageable);
        } else if (userId != null && inicio != null && fim != null) {
            page = consumoRepository.findByUser_IdAndDataConsumoBetween(userId, inicio, fim, pageable);
        } else if (userId != null) {
            page = consumoRepository.findByUser_Id(userId, pageable);
        } else if (productId != null) {
            page = consumoRepository.findByProduto_Id(productId, pageable);
        } else if (inicio != null && fim != null) {
            page = consumoRepository.findByDataConsumoBetween(inicio, fim, pageable);
        } else {
            page = consumoRepository.findAll(pageable);
        }

        return ResponseEntity.ok(page.map(ConsumoResponseDTO::fromEntity));
    }

    // ===================== READ (BY ID) =====================
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        return consumoRepository.findById(id)
                .map(ConsumoResponseDTO::fromEntity)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> notFound("Consumo não encontrado"));
    }

    // ===================== READ (LIST BY USER - sem paginação) =====================
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<?> listByUser(@PathVariable Long userId) {
        List<Consumo> list = consumoRepository.findByUser_Id(userId);
        return ResponseEntity.ok(list.stream().map(ConsumoResponseDTO::fromEntity).toList());
    }

    // ===================== UPDATE =====================
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id, @Valid @RequestBody ConsumoRequestDTO req) {
        User user = userRepository.findById(req.userId())
                .orElseThrow(() -> new NoSuchElementException("Usuario não encontrado"));
        Product product = productRepository.findById(req.productId())
                .orElseThrow(() -> new NoSuchElementException("Produto não encontrado"));

        Consumo toUpdate = new Consumo(
                user,
                product,
                req.consumiuPesquisa(),
                req.pesquisa(),
                req.dataConsumo()
        );

        // OBS: seu service.update(id, consumo) ainda atualiza "pelo user".
        // Recomendo evoluir para atualizar por ID dentro do service.
        ResultService result = consumoService.update(id, toUpdate);
        if (!result.isValid()) return badRequest(result);
        if (!result.isRealized()) return notFound(result);

        Consumo refreshed = consumoRepository
                .findTopByUser_IdOrderByDataConsumoDesc(user.getId())
                .orElse(null);

        if (refreshed == null) return notFound("Consumo não encontrado após atualização");
        return ResponseEntity.ok(ConsumoResponseDTO.fromEntity(refreshed));
    }

    // ===================== DELETE =====================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        ResultService result = consumoService.delete(id);
        if (!result.isValid()) return badRequest(result);
        if (!result.isRealized()) return notFound(result);
        return ResponseEntity.noContent().build();
    }

    // ----------------- Helpers -----------------
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
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiError> handleNotFound(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError(404, "Not Found", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(400, "Bad Request", ex.getMessage()));
    }
}
