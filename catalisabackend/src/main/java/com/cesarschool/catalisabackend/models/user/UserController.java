package com.cesarschool.catalisabackend.models.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
// 🔁 trocado de /api/v1/users para /api/v2/users
@RequestMapping("/api/v2/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    //======================================
    // LOGIN
    //======================================
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO body) {
        boolean ok = userService.authenticate(body.email(), body.password());
        return ResponseEntity.ok(new LoginResponseDTO(ok, ok ? "Autenticado com sucesso" : "Credenciais inválidas"));
    }

    //======================================
    // CREATE
    //======================================
    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserRequestDTO body) {
        UserResponseDTO created = userService.createUser(body);
        // 🔁 atualiza Location para v2
        URI location = URI.create("/api/v2/users/" + created.id());
        return ResponseEntity.created(location).body(created);
    }

    //======================================
    // READ
    //======================================
    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> list(Pageable pageable,
                                                      @RequestParam(required = false) String email,
                                                      @RequestParam(required = false) String username,
                                                      @RequestParam(required = false) String cpfCnpj) {

        Page<UserResponseDTO> page;

        if (email != null && !email.isBlank()) {
            var dto = userRepository.findByEmail(email.trim().toLowerCase())
                    .map(u -> new UserResponseDTO(u.getId(), u.getEmail(), u.getUsername()))
                    .orElse(null);

            page = (dto == null)
                    ? Page.empty(pageable)
                    : new PageImpl<>(List.of(dto), pageable, 1);

        } else if (username != null && !username.isBlank()) {
            var dto = userRepository.findByUsername(username.trim())
                    .map(u -> new UserResponseDTO(u.getId(), u.getEmail(), u.getUsername()))
                    .orElse(null);

            page = (dto == null)
                    ? Page.empty(pageable)
                    : new PageImpl<>(List.of(dto), pageable, 1);

        } else if (cpfCnpj != null && !cpfCnpj.isBlank()) {
            var dto = userRepository.findByCpfCnpj(cpfCnpj.trim())
                    .map(u -> new UserResponseDTO(u.getId(), u.getEmail(), u.getUsername()))
                    .orElse(null);

            page = (dto == null)
                    ? Page.empty(pageable)
                    : new PageImpl<>(List.of(dto), pageable, 1);

        } else {
            page = userRepository.findAll(pageable)
                    .map(u -> new UserResponseDTO(u.getId(), u.getEmail(), u.getUsername()));
        }

        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable Long id) {
        var dto = userRepository.findById(id)
                .map(u -> new UserResponseDTO(u.getId(), u.getEmail(), u.getUsername()))
                .orElseThrow(() -> new NoSuchElementException("Usuario não encontrado"));
        return ResponseEntity.ok(dto);
    }

    //======================================
    // READ - CONSUMOS (v2)
    //======================================
    @GetMapping("/{id}/consumos")
    public ResponseEntity<List<ConsumoResponseDTO>> listUserConsumos(@PathVariable Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuario não encontrado"));

        var consumos = user.getConsumos() == null ? List.<ConsumoResponseDTO>of()
                : user.getConsumos().stream()
                .map(c -> new ConsumoResponseDTO(
                        c.getId(),
                        c.getUser()    != null ? c.getUser().getId()    : null,
                        c.getProduto() != null ? c.getProduto().getId() : null,
                        c.getDataConsumo(),
                        c.isConsumiuPesquisa(),
                        c.getPesquisa() != null ? c.getPesquisa().getId() : null
                ))
                .toList();

        return ResponseEntity.ok(consumos);
    }

    public record ConsumoResponseDTO(
            Long id,
            Long userId,
            Long productId,
            java.time.LocalDate dataConsumo,
            boolean pesquisaRespondida,
            Long pesquisaId
    ) {}

    //======================================
    // UPDATE (password / username)
    //======================================
    public record ChangePasswordDTO(
            @NotBlank String oldPassword,
            @NotBlank String newPassword
    ) {}

    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> changePassword(@PathVariable Long id,
                                               @Valid @RequestBody ChangePasswordDTO body) {
        userService.changePassword(id, body.oldPassword(), body.newPassword());
        return ResponseEntity.noContent().build();
    }

    public record UpdateUsernameDTO(@NotBlank String username) {}

    @PatchMapping("/{id}/username")
    public ResponseEntity<UserResponseDTO> changeUsername(@PathVariable Long id,
                                                          @Valid @RequestBody UpdateUsernameDTO body) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuario não encontrado"));

        String newUsername = body.username().trim();
        if (!newUsername.equals(user.getUsername()) && userRepository.existsByUsername(newUsername)) {
            throw new IllegalArgumentException("Username ja existente");
        }
        user.setUsername(newUsername);
        userRepository.save(user);

        return ResponseEntity.ok(new UserResponseDTO(user.getId(), user.getEmail(), user.getUsername()));
    }

    //======================================
    // DELETE
    //======================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("Usuario não encontrado");
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/by-email/{email}")
    public ResponseEntity<Void> deleteByEmail(@PathVariable String email) {
        long deleted = userRepository.deleteByEmail(email.trim().toLowerCase());
        if (deleted == 0) throw new NoSuchElementException("Usuario não encontrado");
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/by-username/{username}")
    public ResponseEntity<Void> deleteByUsername(@PathVariable String username) {
        long deleted = userRepository.deleteByUsername(username.trim());
        if (deleted == 0) throw new NoSuchElementException("Usuario não encontrado");
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/by-cpf-cnpj/{cpfCnpj}")
    public ResponseEntity<Void> deleteByCpfCnpj(@PathVariable String cpfCnpj) {
        long deleted = userRepository.deleteByCpfCnpj(cpfCnpj.trim());
        if (deleted == 0) throw new NoSuchElementException("Usuario não encontrado");
        return ResponseEntity.noContent().build();
    }

    //======================================
    // EXCEPTIONS
    //======================================
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex) {
        String msg = ex.getMessage();
        if ("User not found".equalsIgnoreCase(msg)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiError("NOT_FOUND", "Usuario não encontrado"));
        }
        if ("Password doesn't match".equalsIgnoreCase(msg)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiError("UNAUTHORIZED", "Credenciais invalidas"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiError("BAD_REQUEST", msg));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiError> handleNotFound(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError("NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class, BindException.class })
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
