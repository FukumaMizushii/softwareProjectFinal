package com.ebookmanagement.config;

import com.ebookmanagement.security.CustomUserDetailsService;
import com.ebookmanagement.security.LoginSuccessHandler;


import org.springframework.boot.web.servlet.server.CookieSameSiteSupplier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Central Spring Security configuration:

 * - which URLs are public vs protected
 * - BCrypt password encoding
 * - form login + role-based redirect
 * - logout
=======
 *  - which URLs are public vs protected
 *  - BCrypt password encoding
 *  - form login + role-based redirect
 *  - logout
 *  - session management (prevents stale-cookie redirect loops)

 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final LoginSuccessHandler loginSuccessHandler;

    public SecurityConfig(CustomUserDetailsService userDetailsService,

            LoginSuccessHandler loginSuccessHandler) {

                          LoginSuccessHandler loginSuccessHandler) {

        this.userDetailsService = userDetailsService;
        this.loginSuccessHandler = loginSuccessHandler;
    }

    /** BCrypt is the required password hashing algorithm. */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /** Wires our user lookup + password encoder into the auth process. */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http

                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(auth -> auth
                        // Public routes
                        .requestMatchers(
                                "/", "/books", "/books/search", "/books/{id}",
                                "/login", "/perform-login", "/register", "/error",
                                "/css/**", "/js/**", "/images/**", "/covers/**")
                        .permitAll()
                        // Admin-only routes
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // Authenticated user routes (USER or ADMIN)
                        .requestMatchers(
                                "/user/**", "/collection/**",
                                "/books/read/**", "/books/download/**")
                        .hasAnyRole("USER", "ADMIN")
                        // Everything else needs authentication
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        // Process the login POST at a DIFFERENT url than the page itself.
                        // Using "/login" for both the page (GET) and the processing (POST)
                        // can confuse the filter chain; a dedicated processing url avoids
                        // the redirect loop entirely.
                        .loginProcessingUrl("/perform-login")
                        .usernameParameter("email") // we log in with email
                        .passwordParameter("password")
                        .successHandler(loginSuccessHandler)
                        .failureUrl("/login?error=true")
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll());

        return http.build();
    }

            .authenticationProvider(authenticationProvider())
            .authorizeHttpRequests(auth -> auth
                // Public routes
                .requestMatchers(
                        "/", "/books", "/books/search", "/books/{id}",
                        "/login", "/perform-login", "/register",
                        "/css/**", "/js/**", "/images/**", "/covers/**",
                        "/uploads/**"
                ).permitAll()
                // Admin-only routes
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // Authenticated user routes (USER or ADMIN)
                .requestMatchers(
                        "/user/**", "/collection/**",
                        "/books/read/**", "/books/download/**"
                ).hasAnyRole("USER", "ADMIN")
                // Everything else needs authentication
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                // Process the login POST at a DIFFERENT url than the page itself.
                // Using "/login" for both the page (GET) and the processing (POST)
                // can confuse the filter chain; a dedicated processing url avoids
                // the redirect loop entirely.
                .loginProcessingUrl("/perform-login")
                .usernameParameter("email")     // we log in with email
                .passwordParameter("password")
                .successHandler(loginSuccessHandler)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .sessionManagement(session -> session
                // Create a brand-new session ID on login; keeps attributes so the
                // request cache works, but a fresh ID breaks any stale-cookie loop.
                .sessionFixation().migrateSession()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            );

        return http.build();
    }

    /**
     * Configures the JSESSIONID cookie with SameSite=Strict so the browser
     * never re-sends a stale cookie on cross-site navigations, eliminating
     * the most common source of redirect loops.
     */
    @Bean
    public CookieSameSiteSupplier cookieSameSiteSupplier() {
        return CookieSameSiteSupplier.ofStrict();
    }
}
