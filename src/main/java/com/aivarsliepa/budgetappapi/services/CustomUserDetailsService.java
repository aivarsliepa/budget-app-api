package com.aivarsliepa.budgetappapi.services;

import com.aivarsliepa.budgetappapi.data.user.UserRepository;
import com.aivarsliepa.budgetappapi.security.UserPrincipal;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    @NonNull
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .map(UserPrincipal::create)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username : " + username));
    }

    public UserDetails loadUserById(Long id) {
        return userRepository
                .findById(id)
                .map(UserPrincipal::create)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id : " + id));
    }
}
