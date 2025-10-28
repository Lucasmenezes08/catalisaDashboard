package com.cesarschool.catalisabackend.user;

import com.cesarschool.catalisabackend.user.dto.UserResponse;

final class UserAppMapper {
    private UserAppMapper() {}
    static UserResponse toResponse(UserApp u) {
        return new UserResponse(u.getId(), u.getName(), u.getEmail());
    }
}