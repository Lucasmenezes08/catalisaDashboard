package com.cesarschool.catalisabackend.models.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Setter @Getter
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Column(nullable = false, unique = true)
    @Setter(AccessLevel.NONE)
    private String CpfCnpj;

    private String username;

    @NotBlank
    @Column(nullable = false)
    @Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
    private String password;

    protected User() {}
    public User(String email, String cpfCnpj, String password) {
        this();
        this.email = email;
        this.CpfCnpj = cpfCnpj;
        this.password = password;
    }
    public User(String email, String cpfCnpj, String username, String password) {
        this(email, cpfCnpj, password);
        this.username = username;
    }

    // Add later list of consumes (Need to create Consume entity first)

    @Override
    public String toString() {
        return String.format("User[id=%d, username='%s', email='%s']", id, username, email);
    }

    public boolean changePassword(String newPassword) {
        if (!this.password.equals(newPassword)) {
            this.password = newPassword;
            return true;
        }
        return false;
    }
}