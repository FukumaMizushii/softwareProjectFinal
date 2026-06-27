package com.ebookmanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/** Carries registration form data and validates it. */
@Data
public class UserRegistrationDto {

    // Named constant instead of a "magic number" for the password rule.
    public static final int MIN_PASSWORD_LENGTH = 6;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = MIN_PASSWORD_LENGTH,
            message = "Password must be at least 6 characters")
    private String password;

    private String phone;

    private String address;
}
