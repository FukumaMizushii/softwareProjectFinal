package com.ebookmanagement.controller;

import com.ebookmanagement.dto.UserRegistrationDto;
import com.ebookmanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Handles the login page display and user self-registration.
 * (The actual login POST is processed by Spring Security.)
 */
@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") UserRegistrationDto dto,
                           BindingResult result,
                           Model model) {
        // Show field validation errors.
        if (result.hasErrors()) {
            return "register";
        }
        try {
            userService.register(dto);
        } catch (IllegalArgumentException ex) {
            // e.g. email already taken
            model.addAttribute("registerError", ex.getMessage());
            return "register";
        }
        return "redirect:/login?registered=true";
    }
}
