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
    private long id;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String cpfCnpj;

    private String username;

    @NotBlank
    @Column(nullable = false)
    @Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
    private String password;

    public User() {}
    public User(String email, String cpfCnp, String password) {
        this();
        this.email = email;
        this.cpfCnpj = cpfCnp;
        this.password = password;
    }
    public User(String email, String cpfCnpj, String username, String password) {
        this(email, cpfCnpj, password);
        this.username = username;
    }
    // Add later list of consumes (Need to create Consume entity first)

    public boolean changePassword(String newPassword) {
        if (!this.password.equals(newPassword)) {
            this.password = newPassword;
            return true;
        }
        return false;
    }
}