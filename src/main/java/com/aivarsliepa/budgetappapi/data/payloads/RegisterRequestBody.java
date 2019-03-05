package com.aivarsliepa.budgetappapi.data.payloads;

import com.aivarsliepa.budgetappapi.data.common.validation.passwordmatches.PasswordMatches;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@PasswordMatches
public class RegisterRequestBody {
    @NotBlank
    String username;

    @NotBlank
    String password;

    @NotBlank
    String confirmPassword;
}
