package com.cesarschool.catalisabackend.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
public class UserApp implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotBlank
    @Column(unique = true, length = 50)
    private String name;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    @Setter(AccessLevel.NONE)
    private String password;

    public UserApp() { super(); }
    public UserApp(String email, String password) {
        this();
        this.email = email;
        this.password = password;
    }
    public UserApp(Long id, String name, String email, String password) {
        this(email, password);
        this.id = id;
        this.name = name;
    }
}