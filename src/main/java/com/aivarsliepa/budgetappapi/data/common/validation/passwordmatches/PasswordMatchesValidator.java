package com.aivarsliepa.budgetappapi.data.common.validation.passwordmatches;

import com.aivarsliepa.budgetappapi.data.payloads.RegisterRequestBody;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, RegisterRequestBody> {
    @Override
    public boolean isValid(RegisterRequestBody data, ConstraintValidatorContext context) {
        var password = data.getPassword();
        return password != null && password.equals(data.getConfirmPassword());
    }
}
