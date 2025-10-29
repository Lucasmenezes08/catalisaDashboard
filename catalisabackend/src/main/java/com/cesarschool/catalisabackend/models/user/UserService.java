package com.cesarschool.catalisabackend.models.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponseDTO createUser(UserCreateDTO createDTO) {
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

    public boolean changePassword(Long id, String oldPassword, String newPassword) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!user.getPassword().equals(oldPassword)) {
            throw new IllegalArgumentException("Old password doesn't match");
        }

        user.changePassword(newPassword);
        userRepository.save(user);

        return true;
    }


}