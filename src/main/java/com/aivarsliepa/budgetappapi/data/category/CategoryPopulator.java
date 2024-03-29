package com.aivarsliepa.budgetappapi.data.category;

import com.aivarsliepa.budgetappapi.data.common.Populator;
import org.springframework.stereotype.Component;

@Component
public class CategoryPopulator implements Populator<CategoryModel, CategoryData> {
    @Override
    public CategoryData populateData(CategoryData target, CategoryModel source) {
        target.setType(source.getType());
        target.setParentId(source.getParentId());
        target.setId(source.getId());
        return target;
    }

    @Override
    public CategoryModel populateModel(CategoryModel target, CategoryData source) {
        target.setType(source.getType());
        target.setParentId(source.getParentId());
        return target;
    }
}
