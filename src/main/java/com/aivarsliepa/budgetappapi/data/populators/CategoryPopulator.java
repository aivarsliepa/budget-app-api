package com.aivarsliepa.budgetappapi.data.populators;

import com.aivarsliepa.budgetappapi.data.dto.CategoryData;
import com.aivarsliepa.budgetappapi.data.models.CategoryModel;

public class CategoryPopulator implements Populator<CategoryModel, CategoryData> {
    @Override
    public CategoryData populateData(CategoryData target, CategoryModel source) {
        target.setCategoryType(source.getCategoryType());
        target.setParentId(source.getParentId());
        target.setId(source.getId());
        return target;
    }

    @Override
    public CategoryModel populateModel(CategoryModel target, CategoryData source) {
        if (null != source.getType()) {
            target.setCategoryType(source.getCategoryType());
        }

        if (null != source.getParentId()) {
            target.setParentId(source.getParentId());
        }

        return target;
    }
}
