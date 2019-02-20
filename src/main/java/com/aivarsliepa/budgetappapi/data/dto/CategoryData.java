package com.aivarsliepa.budgetappapi.data.dto;

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
}
