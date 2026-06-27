package com.ebookmanagement.pattern.factory;

import com.ebookmanagement.dto.UserRegistrationDto;
import com.ebookmanagement.entity.Role;
import com.ebookmanagement.entity.User;

/**
 * ============================================================
 * DESIGN PATTERN: SIMPLE FACTORY
 * ============================================================
 * A "factory" that knows how to build User objects so that the
 * creation logic lives in ONE place instead of being copy-pasted
 * around the codebase (supports the DRY principle).
 *
 * The client (e.g. the service) just asks the factory for a User
 * and does not need to know the step-by-step construction details.
 *
 * Note on responsibilities: password ENCRYPTION is intentionally
 * NOT done here. The factory only assembles the object; the service
 * layer encodes the password. This keeps each part focused
 * (Single Responsibility Principle).
 * ============================================================
 */
public final class UserFactory {

    private UserFactory() {
        // utility class: no instances needed
    }

    /**
     * Builds a normal reader account from a registration form.
     * The password passed in should already be encoded by the caller.
     */
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

    /** Builds an admin account (used by the seed initializer). */
    public static User createAdmin(String name, String email, String encodedPassword) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setRole(Role.ADMIN);
        return user;
    }
}
