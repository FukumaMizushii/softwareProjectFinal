package com.ebookmanagement.controller;

import com.ebookmanagement.dto.CategoryDto;
import com.ebookmanagement.entity.Category;
import com.ebookmanagement.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/** Admin CRUD for categories. */
@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final CategoryService categoryService;

    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String manageCategories(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "admin/categories";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("categoryDto", new CategoryDto());
        return "admin/category-form";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("categoryDto") CategoryDto dto,
                      BindingResult result) {
        if (result.hasErrors()) {
            return "admin/category-form";
        }
        categoryService.save(dto);
        return "redirect:/admin/categories";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Category category = categoryService.findById(id);
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setCategoryName(category.getCategoryName());
        dto.setDescription(category.getDescription());
        model.addAttribute("categoryDto", dto);
        model.addAttribute("editMode", true);
        return "admin/category-form";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       @Valid @ModelAttribute("categoryDto") CategoryDto dto,
                       BindingResult result,
                       Model model) {
        if (result.hasErrors()) {
            model.addAttribute("editMode", true);
            return "admin/category-form";
        }
        categoryService.update(id, dto);
        return "redirect:/admin/categories";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        categoryService.deleteById(id);
        return "redirect:/admin/categories";
    }
}
