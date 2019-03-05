package com.aivarsliepa.budgetappapi.data.payloads;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginRequestBody {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
