package com.aivarsliepa.budgetappapi.data.mappers;

import com.aivarsliepa.budgetappapi.data.dto.CategoryData;
import com.aivarsliepa.budgetappapi.data.models.CategoryModel;
import org.springframework.stereotype.Component;

/**
 * Map CategoryModel DTO to model and vice versa
 */
@Component
public class CategoryMapper {
    public CategoryData map(final CategoryModel model) {
        var data = new CategoryData();

        data.setCategoryType(model.getCategoryType());
        data.setId(model.getId());
        data.setParentId(model.getParentId());

        return data;
    }

    public CategoryModel map(CategoryData data) {
        var model = new CategoryModel();

        model.setParentId(data.getParentId());
        model.setId(data.getId());
        model.setCategoryType(data.getCategoryType());

        return model;
    }
}
