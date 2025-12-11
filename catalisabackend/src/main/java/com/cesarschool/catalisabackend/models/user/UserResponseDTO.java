package com.cesarschool.catalisabackend.models.user;

public record UserResponseDTO(
        Long id,
        String email,
        String username
) {
    public static UserResponseDTO fromEntity(User user) {
        if (user == null) {
            return null;
        }
        return new UserResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getUsername()
        );
    }
}
