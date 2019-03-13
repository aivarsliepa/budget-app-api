package com.aivarsliepa.budgetappapi.services;

import com.aivarsliepa.budgetappapi.data.payloads.JwtAuthResponseBody;
import com.aivarsliepa.budgetappapi.data.payloads.LoginRequestBody;
import com.aivarsliepa.budgetappapi.data.payloads.RegisterRequestBody;
import com.aivarsliepa.budgetappapi.data.user.UserModel;
import com.aivarsliepa.budgetappapi.data.user.UserRepository;
import com.aivarsliepa.budgetappapi.exceptions.UnauthenticatedException;
import com.aivarsliepa.budgetappapi.security.JwtTokenProvider;
import com.aivarsliepa.budgetappapi.security.UserPrincipal;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    @NonNull
    private UserRepository userRepository;

    @NonNull
    private PasswordEncoder passwordEncoder;

    @NonNull
    AuthenticationManager authenticationManager;

    @NonNull
    JwtTokenProvider jwtTokenProvider;

    public JwtAuthResponseBody register(RegisterRequestBody data) {
        var user = new UserModel();
        user.setPassword(passwordEncoder.encode(data.getPassword()));
        user.setUsername(data.getUsername());

        var persistedModel = userRepository.save(user);

        var jwt = jwtTokenProvider.generateTokenFromId(persistedModel.getId());

        return new JwtAuthResponseBody(jwt);
    }

    public JwtAuthResponseBody login(LoginRequestBody data) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        data.getUsername(),
                        data.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        var jwt = jwtTokenProvider.generateToken(authentication);

        return new JwtAuthResponseBody(jwt);
    }

    public UserModel getCurrentUser() {
        var userId = getCurrentUserId();

        return userRepository
                .findById(userId)
                .orElseThrow(() -> new UnauthenticatedException("Could not find user for id: " + userId));
    }

    Long getCurrentUserId() {
        var user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user instanceof UserPrincipal) {
            return ((UserPrincipal) user).getId();
        }

        throw new UnauthenticatedException("User should be authenticated, but is is not");
    }
}
