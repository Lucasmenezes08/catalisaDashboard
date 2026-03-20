package com.cesarschool.catalisabackend.services;

import com.cesarschool.catalisabackend.dtos.request.UserRequestDTO;
import com.cesarschool.catalisabackend.dtos.response.UserResponseDTO;
import com.cesarschool.catalisabackend.models.User;
import com.cesarschool.catalisabackend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserResponseDTO createUser(UserRequestDTO createDTO) {
        String email = createDTO.email().trim().toLowerCase();
        String cpfCnpj = createDTO.cpfCnpj().trim();
        String username = createDTO.username() == null ? null : createDTO.username().trim();

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (userRepository.existsByCpfCnpj(cpfCnpj)) {
            throw new IllegalArgumentException("Cpf/Cnpj already exists");
        }

        User user = new User(email, cpfCnpj, username, createDTO.password());
        userRepository.save(user);

        return new UserResponseDTO(user.getId(), user.getEmail(), user.getUsername());
    }

    @Transactional
    public boolean changePassword(Long id, String oldPassword, String newPassword) {
        if (oldPassword == null || newPassword == null) {
            throw new IllegalArgumentException("Passwords cannot be null");
        }
        if (newPassword.isBlank()) {
            throw new IllegalArgumentException("New password cannot be blank");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!oldPassword.equals(user.getPassword())) {
            throw new IllegalArgumentException("Old password doesn't match");
        }
        if (newPassword.equals(user.getPassword())) {
            throw new IllegalArgumentException("New password must be different from the old one");
        }

        user.changePassword(newPassword);
        userRepository.save(user);
        return true;
    }
    public boolean authenticate(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Password doesn't match");
        }
        return true;
    }
}