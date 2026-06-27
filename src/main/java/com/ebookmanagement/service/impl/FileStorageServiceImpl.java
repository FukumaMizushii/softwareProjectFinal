package com.ebookmanagement.service.impl;

import com.ebookmanagement.exception.FileStorageException;
import com.ebookmanagement.service.FileStorageService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Stores uploaded files on the local filesystem and returns their
 * relative paths (which is what gets saved in the database).
 */
@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path rootLocation;

    public FileStorageServiceImpl(@Value("${file.upload-dir}") String uploadDir) {
        this.rootLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    /** Make sure the upload folders exist when the app starts. */
    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootLocation.resolve("books"));
            Files.createDirectories(rootLocation.resolve("covers"));
        } catch (IOException e) {
            throw new FileStorageException("Could not initialize upload directories", e);
        }
    }

    @Override
    public String storeFile(MultipartFile file, String subFolder) {
        if (file == null || file.isEmpty()) {
            throw new FileStorageException("Cannot store an empty file");
        }
        // Build a unique, safe filename to avoid collisions and path traversal.
        String original = StringUtils.cleanPath(file.getOriginalFilename() == null ? "file" : file.getOriginalFilename());
        String unique = UUID.randomUUID() + "_" + original.replaceAll("\\s+", "_");

        try {
            Path targetDir = rootLocation.resolve(subFolder).normalize();
            Files.createDirectories(targetDir);
            Path targetFile = targetDir.resolve(unique).normalize();

            // Security check: ensure we are still inside the upload root.
            if (!targetFile.startsWith(rootLocation)) {
                throw new FileStorageException("Cannot store file outside upload directory");
            }
            Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);

            // Return a relative path like "books/uuid_name.pdf"
            return subFolder + "/" + unique;
        } catch (IOException e) {
            throw new FileStorageException("Failed to store file " + original, e);
        }
    }

    @Override
    public Resource loadAsResource(String relativePath) {


        if (relativePath == null || relativePath.isBlank()) {
            throw new FileStorageException("File path is not specified");
        }

        try {
            Path file = rootLocation.resolve(relativePath).normalize();
            if (!file.startsWith(rootLocation)) {
                throw new FileStorageException("Access outside upload directory is not allowed");
            }
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            }
            throw new FileStorageException("Could not read file: " + relativePath);
        } catch (MalformedURLException e) {
            throw new FileStorageException("Could not read file: " + relativePath, e);
        }
    }
}
