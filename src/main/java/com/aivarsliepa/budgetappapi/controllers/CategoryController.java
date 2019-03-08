package com.aivarsliepa.budgetappapi.controllers;

import com.aivarsliepa.budgetappapi.constants.URLPaths;
import com.aivarsliepa.budgetappapi.data.category.CategoryData;
import com.aivarsliepa.budgetappapi.services.CategoryService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(URLPaths.Categories.BASE)
@RequiredArgsConstructor
public class CategoryController {
    @NonNull
    private CategoryService categoryService;

    @GetMapping
    public List<CategoryData> getList() {
        return categoryService.getListForCurrentUser();
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
    @ResponseStatus(HttpStatus.OK)
    public void updateById(@Valid @RequestBody final CategoryData category, @PathVariable final Long categoryId) {
        categoryService.updateById(categoryId, category);
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable final Long categoryId) {
        categoryService.deleteById(categoryId);
    }
}
