package com.cesarschool.catalisabackend.user.dto;

public record UserResponse(
        Long id,
        String name,
        String email
) {}