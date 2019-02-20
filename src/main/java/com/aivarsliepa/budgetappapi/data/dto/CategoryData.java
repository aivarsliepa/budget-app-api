package com.aivarsliepa.budgetappapi.data.dto;

import com.aivarsliepa.budgetappapi.data.enums.CategoryType;
import com.aivarsliepa.budgetappapi.data.validation.categorytype.CategoryTypeConstraint;
import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class CategoryData {
    private Long id;

    @NotNull
    @CategoryTypeConstraint
    private String type;

    private Long parentId;

    public CategoryType getCategoryType() {
        return CategoryType.valueOf(type);
    }

    public void setCategoryType(final CategoryType categoryType) {
        this.type = categoryType.toString();
    }
}
