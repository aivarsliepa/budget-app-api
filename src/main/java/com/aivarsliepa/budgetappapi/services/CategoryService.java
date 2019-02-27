package com.aivarsliepa.budgetappapi.services;

import com.aivarsliepa.budgetappapi.data.category.CategoryData;
import com.aivarsliepa.budgetappapi.data.category.CategoryModel;
import com.aivarsliepa.budgetappapi.data.category.CategoryPopulator;
import com.aivarsliepa.budgetappapi.data.category.CategoryRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    @NonNull
    private CategoryRepository categoryRepository;

    @NonNull
    private CategoryPopulator categoryPopulator;

    public CategoryData create(CategoryData categoryData) {
        var model = categoryPopulator.populateModel(new CategoryModel(), categoryData);

        var persistedModel = categoryRepository.save(model);

        return categoryPopulator.populateData(new CategoryData(), persistedModel);
    }

    public Optional<CategoryData> updateById(Long id, CategoryData categoryData) {
        return categoryRepository.findById(id).map(model -> {
            categoryPopulator.populateModel(model, categoryData);
            var updatedModel = categoryRepository.save(model);
            return categoryPopulator.populateData(new CategoryData(), updatedModel);
        });
    }

    public boolean deleteById(Long id) {
        if (!categoryRepository.existsById(id)) {
            return false;
        }

        categoryRepository.deleteById(id);
        return true;
    }

    public List<CategoryData> findAll() {
        return categoryRepository
                .findAll()
                .stream()
                .map(model -> categoryPopulator.populateData(new CategoryData(), model))
                .collect(Collectors.toList());
    }

    public Optional<CategoryData> findById(Long id) {
        return categoryRepository
                .findById(id)
                .map((model -> categoryPopulator.populateData(new CategoryData(), model)));
    }
}
