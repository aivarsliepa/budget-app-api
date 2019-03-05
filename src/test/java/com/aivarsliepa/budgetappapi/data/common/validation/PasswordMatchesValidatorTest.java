package com.aivarsliepa.budgetappapi.data.common.validation;

import com.aivarsliepa.budgetappapi.data.payloads.RegisterRequestBody;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.Validation;
import javax.validation.Validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
public class PasswordMatchesValidatorTest {
    private Validator validator;

    private RegisterRequestBody data;

    @Before
    public void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        data = new RegisterRequestBody();
        data.setUsername("test");
    }

    @Test
    public void shouldNotBeValid_whenPasswords_doesNotMatch() {
        data.setPassword("pw1");
        data.setConfirmPassword("different");

        var violations = validator.validate(data);

        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldBeValid_whenPasswords_doesMatch() {
        data.setPassword("same");
        data.setConfirmPassword("same");

        var violations = validator.validate(data);

        assertTrue(violations.isEmpty());
    }
}
