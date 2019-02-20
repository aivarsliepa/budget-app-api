package com.aivarsliepa.budgetappapi.controllers;

import com.aivarsliepa.budgetappapi.data.models.CategoryModel;
import com.aivarsliepa.budgetappapi.data.repositories.CategoryRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoriesController {

    @NonNull
    private CategoryRepository categoryRepository;

    @GetMapping
    public List<CategoryModel> getCategoryList() {
        return categoryRepository.findAll();
    }

    @PostMapping
    public CategoryModel postCategory(@Valid @RequestBody final CategoryModel category) {
        return categoryRepository.save(category);
    }

    // TODO use id as path variable
    @DeleteMapping
    public void deleteCategory(@RequestBody final CategoryModel category) {
        categoryRepository.delete(category);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryModel> getCategory(@PathVariable final Long categoryId) {
        return categoryRepository.findById(categoryId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{categoryId}")
    public CategoryModel updateCategory(@RequestBody final CategoryModel category, @PathVariable final Long categoryId) {
        category.setId(categoryId);
        return categoryRepository.save(category);
    }

    @DeleteMapping("/{categoryId}")
    public void deleteCategory(@PathVariable final Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
