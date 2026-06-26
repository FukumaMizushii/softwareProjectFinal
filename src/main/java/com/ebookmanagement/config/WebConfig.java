package com.ebookmanagement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

/**
 * Exposes the uploaded cover images so the browser can display them.
 * A request to /covers/** maps to the uploads/covers folder on disk.
 * (Book PDF files are NOT exposed this way; they go through a secured
 *  controller so only logged-in users can read/download them.)
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final String uploadDir;

    public WebConfig(@Value("${file.upload-dir}") String uploadDir) {
        this.uploadDir = uploadDir;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String coversPath = Paths.get(uploadDir, "covers").toAbsolutePath().normalize().toUri().toString();
        registry.addResourceHandler("/covers/**")
                .addResourceLocations(coversPath);
    }
}
