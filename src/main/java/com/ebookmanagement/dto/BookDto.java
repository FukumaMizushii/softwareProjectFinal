package com.ebookmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/** Carries add/edit book form data including uploaded files. */
@Data
public class BookDto {

    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    private String description;

    @PositiveOrZero(message = "Price cannot be negative")
    private Double price;

    @NotNull(message = "Please select a category")
    private Long categoryId;

    // Files are optional on edit (keep existing if not re-uploaded).
    private MultipartFile bookFile;

    private MultipartFile coverImage;

    // Existing paths preserved across an edit when no new file is uploaded.
    private String existingFilePath;
    private String existingCoverImagePath;
}
