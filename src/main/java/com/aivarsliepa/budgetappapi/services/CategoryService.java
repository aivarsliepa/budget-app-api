package com.aivarsliepa.budgetappapi.services;

import com.aivarsliepa.budgetappapi.data.category.CategoryData;
import com.aivarsliepa.budgetappapi.data.category.CategoryModel;
import com.aivarsliepa.budgetappapi.data.category.CategoryPopulator;
import com.aivarsliepa.budgetappapi.data.category.CategoryRepository;
import com.aivarsliepa.budgetappapi.exceptions.ResourceNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    @NonNull
    private CategoryRepository categoryRepository;

    @NonNull
    private CategoryPopulator categoryPopulator;

    @NonNull
    private AuthService authService;

    public CategoryData create(CategoryData categoryData) {
        var model = categoryPopulator.populateModel(new CategoryModel(), categoryData);
        model.setUser(authService.getCurrentUser());

        var persistedModel = categoryRepository.save(model);

        return categoryPopulator.populateData(new CategoryData(), persistedModel);
    }

    public void updateById(Long id, CategoryData data) {
        var userId = authService.getCurrentUserId();

        var model = categoryRepository
                .findByIdAndUserId(id, userId)
                .orElseThrow(() -> buildResourceNotFoundException(id, userId));

        categoryPopulator.populateModel(model, data);
    }

    public void deleteById(Long id) {
        var userId = authService.getCurrentUserId();

        if (!categoryRepository.existsByIdAndUserId(id, userId)) {
            throw buildResourceNotFoundException(id, userId);
        }

        categoryRepository.deleteByIdAndUserId(id, userId);
    }

    public List<CategoryData> getListForCurrentUser() {
        return categoryRepository
                .findAllByUserId(authService.getCurrentUserId())
                .stream()
                .map(model -> categoryPopulator.populateData(new CategoryData(), model))
                .collect(Collectors.toList());
    }

    public CategoryData findById(Long id) {
        var userId = authService.getCurrentUserId();

        return categoryRepository
                .findByIdAndUserId(id, userId)
                .map((model -> categoryPopulator.populateData(new CategoryData(), model)))
                .orElseThrow(() -> buildResourceNotFoundException(id, userId));
    }

    private ResourceNotFoundException buildResourceNotFoundException(Long id, Long userId) {
        var msg = "Category not found with ID: " + id + "; userID: " + userId;
        return new ResourceNotFoundException(msg);
    }
}
