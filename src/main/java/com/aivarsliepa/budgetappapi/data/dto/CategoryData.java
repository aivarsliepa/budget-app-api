package com.aivarsliepa.budgetappapi.data.dto;

import com.aivarsliepa.budgetappapi.data.enums.CategoryType;
import com.aivarsliepa.budgetappapi.data.validation.categorytype.CategoryTypeConstraint;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotEmpty;


@Data
public class CategoryData {
    private Long id;

    @NotEmpty
    @CategoryTypeConstraint
    private String type;

    private Long parentId;

    @JsonIgnore
    public CategoryType getCategoryType() {
        return CategoryType.valueOf(type);
    }

    public void setCategoryType(final CategoryType categoryType) {
        this.type = categoryType.toString();
    }
}
