package com.cesarschool.catalisabackend.models.user;

import com.cesarschool.catalisabackend.models.consumo.Consumo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Entity
@Access(AccessType.FIELD)
@Setter @Getter
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Column(nullable = false, unique = true)
    @Setter(AccessLevel.NONE)
    private String cpfCnpj;

    private String username;

    @NotBlank
    @Column(nullable = false)
    @Setter(AccessLevel.NONE)
    private String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonIgnore
    private List<Consumo> consumos;

    protected User() {}

    public User(String email, String cpfCnpj, String password) {
        this();
        this.email = email;
        this.cpfCnpj = cpfCnpj;
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