package com.ebookmanagement.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    /** Stores a file under the given sub-folder and returns its relative path. */
    String storeFile(MultipartFile file, String subFolder);

    /** Loads a stored file as a downloadable/readable Resource. */
    Resource loadAsResource(String relativePath);
}
