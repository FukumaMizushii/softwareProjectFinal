package com.ebookmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/** Carries add/edit category form data. */
@Data
public class CategoryDto {

    private Long id;

    @NotBlank(message = "Category name is required")
    private String categoryName;

    private String description;
}
