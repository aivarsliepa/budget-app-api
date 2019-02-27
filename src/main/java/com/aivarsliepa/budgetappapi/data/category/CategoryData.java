package com.aivarsliepa.budgetappapi.data.category;

import com.aivarsliepa.budgetappapi.data.common.enums.CategoryType;
import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class CategoryData {
    private Long id;

    @NotNull
    private CategoryType type;

    private Long parentId;
}
