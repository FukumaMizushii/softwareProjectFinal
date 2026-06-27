package com.ebookmanagement.service;

import com.ebookmanagement.dto.CategoryDto;
import com.ebookmanagement.entity.Category;

import java.util.List;

public interface CategoryService {
    Category save(CategoryDto dto);
    Category update(Long id, CategoryDto dto);
    List<Category> findAll();
    Category findById(Long id);
    void deleteById(Long id);
}
