package com.aivarsliepa.budgetappapi.controllers;

import com.aivarsliepa.budgetappapi.data.dto.CategoryData;
import com.aivarsliepa.budgetappapi.services.CategoryService;
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
    private CategoryService categoryService;

    @GetMapping
    public List<CategoryData> getList() {
        return categoryService.findAll();
    }

    @PostMapping
    public CategoryData create(@Valid @RequestBody final CategoryData category) {
        return categoryService.create(category);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryData> getById(@PathVariable final Long categoryId) {
        return categoryService.findById(categoryId)
                              .map(ResponseEntity::ok)
                              .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{categoryId}")
    public ResponseEntity<CategoryData> updateById(@Valid @RequestBody final CategoryData category,
                                                       @PathVariable final Long categoryId) {
        return categoryService.updateById(categoryId, category)
                              .map(ResponseEntity::ok)
                              .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity deleteById(@PathVariable final Long categoryId) {
        var found = categoryService.deleteById(categoryId);

        if (found) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }
}
