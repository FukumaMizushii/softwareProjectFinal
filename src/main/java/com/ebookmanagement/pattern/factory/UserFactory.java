package com.ebookmanagement.pattern.factory;

import com.ebookmanagement.dto.UserRegistrationDto;
import com.ebookmanagement.entity.Role;
import com.ebookmanagement.entity.User;


public final class UserFactory {

    private UserFactory() {

    }

    public static User createReader(UserRegistrationDto dto, String encodedPassword) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(encodedPassword);
        user.setPhone(dto.getPhone());
        user.setAddress(dto.getAddress());
        user.setRole(Role.USER);
        return user;
    }

    public static User createAdmin(String name, String email, String encodedPassword) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setRole(Role.ADMIN);
        return user;
    }
}
