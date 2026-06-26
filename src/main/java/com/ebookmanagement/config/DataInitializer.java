package com.ebookmanagement.config;

import com.ebookmanagement.entity.Category;
import com.ebookmanagement.entity.User;
import com.ebookmanagement.repository.CategoryRepository;
import com.ebookmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Runs once at startup to create a default admin account and a few
 * starter categories, so you can log in immediately after first run.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;

    private final String adminEmail;
    private final String adminPassword;
    private final String adminName;

    public DataInitializer(UserRepository userRepository,
                           CategoryRepository categoryRepository,
                           PasswordEncoder passwordEncoder,
                           @Value("${app.admin.email}") String adminEmail,
                           @Value("${app.admin.password}") String adminPassword,
                           @Value("${app.admin.name}") String adminName) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
        this.adminName = adminName;
    }

    @Override
    public void run(String... args) {
        // Create the seed admin only if it doesn't already exist.
        if (!userRepository.existsByEmail(adminEmail)) {
            // SIMPLE FACTORY builds the admin user.
            User admin = com.ebookmanagement.pattern.factory.UserFactory
                    .createAdmin(adminName, adminEmail, passwordEncoder.encode(adminPassword));
            userRepository.save(admin);
            System.out.println(">>> Seed admin created: " + adminEmail + " / " + adminPassword);
        }

        // Add a few default categories on a fresh database.
        if (categoryRepository.count() == 0) {
            createCategory("Fiction", "Novels and short stories");
            createCategory("Science", "Scientific and technical books");
            createCategory("History", "Historical works");
            createCategory("Technology", "Programming and computing");
            System.out.println(">>> Default categories created");
        }
    }

    private void createCategory(String name, String description) {
        Category c = new Category();
        c.setCategoryName(name);
        c.setDescription(description);
        categoryRepository.save(c);
    }
}
