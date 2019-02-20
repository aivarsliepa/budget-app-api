package com.aivarsliepa.budgetappapi.data.validation.categorytype;

import com.aivarsliepa.budgetappapi.data.enums.CategoryType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class CategoryTypeValidator implements ConstraintValidator<CategoryTypeConstraint, String> {
    private static final List<String> VALID_TYPES;

    static {
        VALID_TYPES = Stream.of(CategoryType.values())
                            .map(CategoryType::toString)
                            .collect(Collectors.toList());
    }


    @Override
    public boolean isValid(String field,
                           ConstraintValidatorContext cxt) {
        return VALID_TYPES.contains(field);
    }
}