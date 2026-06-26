package com.ebookmanagement.service;

import com.ebookmanagement.dto.UserRegistrationDto;
import com.ebookmanagement.entity.User;

import java.util.List;

public interface UserService {
    User register(UserRegistrationDto dto);
    User findByEmail(String email);
    List<User> findAll();
    User findById(Long id);
    void deleteById(Long id);
}
