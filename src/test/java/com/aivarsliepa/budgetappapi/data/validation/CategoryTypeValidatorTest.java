package com.aivarsliepa.budgetappapi.data.validation;

import com.aivarsliepa.budgetappapi.data.dto.CategoryData;
import com.aivarsliepa.budgetappapi.data.enums.CategoryType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.Validation;
import javax.validation.Validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
public class CategoryTypeValidatorTest {
    private Validator validator;

    @Before
    public void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void shouldNotBeValid_whenType_isNull() {
        var category = new CategoryData();

        var violations = validator.validate(category);

        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldNotBeValid_whenType_isInvalid() {
        var category = new CategoryData();
        category.setType("SOMETHING_INVALID");

        var violations = validator.validate(category);

        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldBeValid_whenType_isExpense() {
        var category = new CategoryData();
        category.setType(CategoryType.EXPENSE.toString());

        var violations = validator.validate(category);

        assertTrue(violations.isEmpty());
    }

    @Test
    public void shouldBeValid_whenType_isIncome() {
        var category = new CategoryData();
        category.setType(CategoryType.INCOME.toString());

        var violations = validator.validate(category);

        assertTrue(violations.isEmpty());
    }

}
