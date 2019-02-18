package com.aivarsliepa.budgetappapi.controllers;

import com.aivarsliepa.budgetappapi.models.Category;
import com.aivarsliepa.budgetappapi.repositories.CategoryRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoriesController {

    @NonNull
    private CategoryRepository categoryRepository;

    @GetMapping
    public List<Category> getCategoryList() {
        return categoryRepository.findAll();
    }

    @PostMapping
    public Category postCategory(@RequestBody final Category category) {
        return categoryRepository.save(category);
    }

    @DeleteMapping
    public void deleteCategory(@RequestBody final Category category) {
        categoryRepository.delete(category);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> getCategory(@PathVariable final Long categoryId) {
        return categoryRepository.findById(categoryId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{categoryId}")
    public Category updateCategory(@RequestBody final Category category, @PathVariable final Long categoryId) {
        category.setId(categoryId);
        return categoryRepository.save(category);
    }

    @DeleteMapping("/{categoryId}")
    public void deleteCategory(@PathVariable final Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
