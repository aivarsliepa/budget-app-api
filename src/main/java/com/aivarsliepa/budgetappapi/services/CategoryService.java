package com.aivarsliepa.budgetappapi.services;

import com.aivarsliepa.budgetappapi.data.dto.CategoryData;
import com.aivarsliepa.budgetappapi.data.mappers.CategoryMapper;
import com.aivarsliepa.budgetappapi.data.repositories.CategoryRepository;
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
    private CategoryMapper categoryMapper;

    public CategoryData create(CategoryData categoryData) {
        categoryData.setId(null); // make sure to not override existing
        var model = categoryMapper.map(categoryData);

        var persistedModel = categoryRepository.save(model);

        return categoryMapper.map(persistedModel);
    }

    public Optional<CategoryData> updateById(Long id, CategoryData categoryData) {
        if (!categoryRepository.existsById(id)) {
            return Optional.empty();
        }

        categoryData.setId(id);
        var model = categoryMapper.map(categoryData);
        var persistedModel = categoryRepository.save(model);
        var updatedData = categoryMapper.map(persistedModel);

        return Optional.of(updatedData);
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
                .map(categoryMapper::map)
                .collect(Collectors.toList());
    }

    public Optional<CategoryData> findById(Long id) {
        return categoryRepository
                .findById(id)
                .map(categoryMapper::map);
    }
}
