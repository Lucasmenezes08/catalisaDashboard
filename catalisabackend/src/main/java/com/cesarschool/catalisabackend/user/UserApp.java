package com.cesarschool.catalisabackend.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Getter @Setter
@Table(name = "users")
public class UserApp implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @NotBlank @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 60)
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private String password;

    public UserApp() {}

    public UserApp(String email, String encodedPassword) {
        this.email = email;
        this.password = encodedPassword;
    }

    public UserApp(Long id, String name, String email, String encodedPassword) {
        this(email, encodedPassword);
        this.id = id;
        this.name = name;
    }

    @Override public String getPassword() { return password; }
    @Override public String getUsername() { return email; }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return List.of(); }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}