package com.aivarsliepa.budgetappapi.controllers;

import com.aivarsliepa.budgetappapi.constants.URLPaths;
import com.aivarsliepa.budgetappapi.data.payloads.JwtAuthResponseBody;
import com.aivarsliepa.budgetappapi.data.payloads.LoginRequestBody;
import com.aivarsliepa.budgetappapi.data.payloads.RegisterRequestBody;
import com.aivarsliepa.budgetappapi.services.AuthService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(URLPaths.Auth.BASE)
@RequiredArgsConstructor
public class AuthController {
    @NonNull
    private AuthService authService;

    @PostMapping(URLPaths.Auth.REGISTER)
    public JwtAuthResponseBody register(@Valid @RequestBody RegisterRequestBody data) {
        return authService.register(data);
    }

    @PostMapping(URLPaths.Auth.LOGIN)
    public JwtAuthResponseBody authenticateUser(@Valid @RequestBody LoginRequestBody data) {
        return authService.login(data);
    }
}
