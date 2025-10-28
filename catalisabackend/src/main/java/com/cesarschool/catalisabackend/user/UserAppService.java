package com.cesarschool.catalisabackend.user;

import com.cesarschool.catalisabackend.user.dto.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserAppService {
    UserResponse register(CreateUserRequest req);
    UserResponse getById(Long id);
    Page<UserResponse> list(Pageable pageable);
    UserResponse update(Long id, UpdateUserRequest req);
    void changePassword(Long id, ChangePasswordRequest req);
    void delete(Long id);
}