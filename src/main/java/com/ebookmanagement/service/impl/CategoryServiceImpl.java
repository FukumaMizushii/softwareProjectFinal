package com.ebookmanagement.service.impl;

import com.ebookmanagement.dto.CategoryDto;
import com.ebookmanagement.entity.Category;
import com.ebookmanagement.exception.ResourceNotFoundException;
import com.ebookmanagement.repository.CategoryRepository;
import com.ebookmanagement.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public Category save(CategoryDto dto) {
        Category category = new Category();
        category.setCategoryName(dto.getCategoryName());
        category.setDescription(dto.getDescription());
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Category update(Long id, CategoryDto dto) {
        Category category = findById(id);
        category.setCategoryName(dto.getCategoryName());
        category.setDescription(dto.getDescription());
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + id));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id " + id);
        }
        categoryRepository.deleteById(id);
    }
}
