package com.cesarschool.catalisabackend.models.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    Optional<User> findByCpfCnpj(String cpfCnpj);
    boolean existsByCpfCnpj(String cpfCnpj);

    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

    long deleteByEmail(String email);
    long deleteByCpfCnpj(String cpfCnpj);
    long deleteByUsername(String username);

}
