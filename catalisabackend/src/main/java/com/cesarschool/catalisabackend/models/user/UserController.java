package com.cesarschool.catalisabackend.models.user;

import com.cesarschool.catalisabackend.models.consumo.ConsumoRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * API REST para User, pronta para o front consumir.
 *
 * Endpoints:
 *  - POST   /api/v1/users
 *  - GET    /api/v1/users
 *  - GET    /api/v1/users/{id}
 *  - PATCH  /api/v1/users/{id}/password
 *  - PATCH  /api/v1/users/{id}/username
 *  - DELETE /api/v1/users/{id}
 *  - DELETE /api/v1/users/by-email/{email}
 *  - DELETE /api/v1/users/by-username/{username}
 *  - DELETE /api/v1/users/by-cpf-cnpj/{cpfCnpj}
 *  - POST   /api/v1/users/login           (autenticação)
 *
 * Filtros opcionais no GET paginado:
 *  - /api/v1/users?email={email}
 *  - /api/v1/users?username={username}
 *  - /api/v1/users?cpfCnpj={cpfCnpj}
 *
 * DTOs:
 *  - UserRequestDTO      : { "email", "cpfCnpj", "username", "password" }
 *  - UserResponseDTO     : { "id", "email", "username" }
 *  - ChangePasswordDTO   : { "oldPassword", "newPassword" } (para /{id}/password)
 *  - UpdateUsernameDTO   : { "username" } (para /{id}/username)
 *  - LoginRequestDTO     : { "email", "password" } (para /login)
 *  - LoginResponseDTO    : { "authenticated", "message" } (retorno do /login)
 *
 * Regras:
 *  - email e cpfCnpj são únicos. username é opcional, mas se enviado também deve ser único.
 *  - Senha é alterada apenas via PATCH /{id}/password (valida oldPassword != newPassword).
 *  - Respostas de erro padronizadas via ApiError { code, message }.
 *
 * Autenticação (/login):
 *  - POST /api/v1/users/login
 *    Body: { "email": "ana@exemplo.com", "password": "Strong@123" }
 *    Respostas:
 *      200 OK            -> { "authenticated": true,  "message": "Authenticated" }
 *      401 Unauthorized  -> { "authenticated": false, "message": "Invalid credentials" }
 *      404 Not Found     -> { "authenticated": false, "message": "User not found" }
 *
 * Exemplos:
 *  - Criar usuário:
 *      POST /api/v1/users
 *      Body:
 *      {
 *        "email": "ana@exemplo.com",
 *        "cpfCnpj": "12345678900",
 *        "username": "aninha",
 *        "password": "Strong@123"
 *      }
 *      201 Created + Location: /api/v1/users/{id}
 *
 *  - Listar paginado:
 *      GET /api/v1/users?page=0&size=10&sort=email,asc
 *
 *  - Buscar por ID:
 *      GET /api/v1/users/1
 *
 *  - Filtrar por e-mail:
 *      GET /api/v1/users?email=ana@exemplo.com
 *
 *  - Trocar senha:
 *      PATCH /api/v1/users/1/password
 *      Body:
 *      { "oldPassword": "Strong@123", "newPassword": "NewStronger@123" }
 *      204 No Content
 *
 *  - Atualizar username:
 *      PATCH /api/v1/users/1/username
 *      Body:
 *      { "username": "ana.silva" }
 *
 *  - Deletar por ID:
 *      DELETE /api/v1/users/1
 *
 *  - Deletar por e-mail/username/cpfCnpj:
 *      DELETE /api/v1/users/by-email/ana@exemplo.com
 *      DELETE /api/v1/users/by-username/ana.silva
 *      DELETE /api/v1/users/by-cpf-cnpj/12345678900
 *
 * Observações:
 *  - Para os deletes por campo, o repositório deve retornar long:
 *      long deleteByEmail(String email);
 *      long deleteByUsername(String username);
 *      long deleteByCpfCnpj(String cpfCnpj);
 *    O controller retorna 404 se nada foi removido.
 *
 * Segurança (recomendado):
 *  - Use hash de senha (BCrypt) no service antes de salvar/comparar.
 *  - Em produção, prefira tokens (JWT) ou sessão; evite retornar dados sensíveis no login.
 *
 *
 *  GET /api/v1/users/{id}/consumos
 * Retorna todas as ocorrências de consumo (historico do usuário) associadas ao usuário identificado por {id}.
 * Descrição
 * Busca o usuário por id.
 * Se existir, carrega os Consumos (Consumo) relacionados a ele, converte cada um para ConsumoResponseDTO e devolve em uma lista.
 * Sem paginação nesta versão (retorna lista completa).
 * Path Params
 * id (Long) — ID do usuário.
 * Responses
 * 200 OK
 // Corpo: List<ConsumoResponseDTO>
 * 404 Not Found
 * Quando o usuário não é encontrado.
 * Corpo: ApiError { code: "NOT_FOUND", message: "Usuario não encontrado" }
 * 500 Internal Server Error
 * Erros inesperados.
 * Schema: ConsumoResponseDTO
 * {
 *   "id": 1,
 //   "user": { /* objeto User vinculado ao consumo */ //},
//        *"product":{ /* objeto Product consumido */ },
//        *"dataConsumo":"2025-10-31",
//        *"avaliacao":5,
//        *"pesquisaRespondida":true,
//        *"pesquisa":{ /* objeto Pesquisa vinculado (1:1) ou null */ }
//        *}
//        *
//        *
//        *Observação:
//        por padrão, o
//DTO atual
//retorna os
//objetos completos
//        de user, product
//e pesquisa.
//Se o
//front preferir
//respostas mais
//
//leves(apenas IDs ou campos essenciais),crie um DTO “slim”
//específico para
//listagem
// */


@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final ConsumoRepository consumoRepository;

    //====== LOGIN ==============================================================
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO body) {
        //função lança IllegalArgumentException com mensagens específicas
        boolean ok = userService.authenticate(body.email(), body.password());
        return ResponseEntity.ok(new LoginResponseDTO(true, "Autenticado com sucesso"));
    }

    // ==== CREATE =============================================================

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserRequestDTO body) {
        UserResponseDTO created = userService.createUser(body);
        URI location = URI.create("/api/v1/users/" + created.id());
        return ResponseEntity.created(location).body(created);
    }

    // ==== READ ===============================================================

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
    @GetMapping("/{id}/consumos")
    public ResponseEntity<java.util.List<com.cesarschool.catalisabackend.models.consumo.ConsumoResponseDTO>>
    listUserConsumos(@PathVariable Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuario não encontrado"));

        var consumos = consumoRepository.findByUser_Id(user.getId())
                .stream()
                .map(c -> new com.cesarschool.catalisabackend.models.consumo.ConsumoResponseDTO(
                        c.getId(),
                        c.getUser(),
                        c.getProduct(),
                        c.getDataConsumo(),
                        c.getAvaliacao(),
                        c.isPesquisaRespondida(),
                        c.getPesquisa()
                ))
                .toList();

        return ResponseEntity.ok(consumos);
    }
    // ==== UPDATE (password) ==================================================
    // === Handler específico para erros de autenticação (adicione na seção de EXCEPTIONS) ===
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleAuthIllegalArgument(IllegalArgumentException ex) {
        String msg = ex.getMessage();
        if ("User not found".equalsIgnoreCase(msg)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiError("NOT_FOUND", "Usuario não encontrado"));
        }
        if ("Password doesn't match".equalsIgnoreCase(msg)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiError("UNAUTHORIZED", "Credenciais invalidas"));
        }
        // fallback: mantém BAD_REQUEST para outras IllegalArgumentException
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiError("BAD_REQUEST", msg));
    }
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

    // (Opcional) Atualizar username
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

    // ==== DELETE =============================================================

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

    // ==== EXCEPTIONS =========================================================

    @ExceptionHandler({
            IllegalArgumentException.class
    })
    public ResponseEntity<ApiError> handleBadRequest(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiError("BAD_REQUEST", ex.getMessage()));
    }

    @ExceptionHandler({
            NoSuchElementException.class
    })
    public ResponseEntity<ApiError> handleNotFound(RuntimeException ex) {
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
