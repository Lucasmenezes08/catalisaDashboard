package com.cesarschool.catalisabackend.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAppRepository extends JpaRepository<UserApp, Long> {
    Optional<UserApp> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByName(String name);

    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsByNameAndIdNot(String name, Long id);
}

