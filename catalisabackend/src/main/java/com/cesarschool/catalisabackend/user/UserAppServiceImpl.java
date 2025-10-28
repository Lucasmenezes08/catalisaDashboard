package com.cesarschool.catalisabackend.user;

import com.cesarschool.catalisabackend.common.ConflictException;
import com.cesarschool.catalisabackend.common.NotFoundException;
import com.cesarschool.catalisabackend.user.dto.ChangePasswordRequest;
import com.cesarschool.catalisabackend.user.dto.CreateUserRequest;
import com.cesarschool.catalisabackend.user.dto.UpdateUserRequest;
import com.cesarschool.catalisabackend.user.dto.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.cesarschool.catalisabackend.user.UserAppMapper.toResponse;

@Service
@Transactional
public class UserAppServiceImpl implements UserAppService {

    private final UserAppRepository repo;
    private final PasswordEncoder encoder;

    public UserAppServiceImpl(UserAppRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Override
    public UserResponse register(CreateUserRequest req) {
        if (repo.existsByEmail(req.email())) throw new ConflictException("Email already in use");
        if (repo.existsByName(req.name()))  throw new ConflictException("Name already in use");

        String hash = encoder.encode(req.password());
        var user = new UserApp(req.email(), hash);
        user.setName(req.name());

        return toResponse(repo.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getById(Long id) {
        var user = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> list(Pageable pageable) {
        return repo.findAll(pageable).map(UserAppMapper::toResponse);
    }

    @Override
    public UserResponse update(Long id, UpdateUserRequest req) {
        var user = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (repo.existsByEmailAndIdNot(req.email(), id)) throw new ConflictException("Email already in use");
        if (repo.existsByNameAndIdNot(req.name(), id))   throw new ConflictException("Name already in use");

        user.setName(req.name());
        user.setEmail(req.email());
        // No explicit save needed; JPA dirty checking will flush on tx commit
        return toResponse(user);
    }

    @Override
    public void changePassword(Long id, ChangePasswordRequest req) {
        var user = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!encoder.matches(req.currentPassword(), user.getPassword())) {
            throw new ConflictException("Current password is incorrect");
        }

        String newHash = encoder.encode(req.newPassword());
        user.setEncodedPassword(newHash); // <-- controlled setter (no reflection)
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new NotFoundException("User not found");
        repo.deleteById(id);
    }
}
