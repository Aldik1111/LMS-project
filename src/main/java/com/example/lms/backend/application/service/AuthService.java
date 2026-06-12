package com.example.lms.backend.application.service;

import com.example.lms.backend.application.dto.LoginRequest;
import com.example.lms.backend.application.dto.LoginResponse;
import com.example.lms.backend.domain.entity.User;
import com.example.lms.backend.domain.repository.UserRepository;
import com.example.lms.backend.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public LoginResponse login(LoginRequest request) {
        try { // проверка емайл + пароль
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(), request.getPassword()));
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid username and password");
        }
        // Load user from DB
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check active account
        if (!user.isActive()) {
            throw new RuntimeException("User is deactivated");
        }

        // Generation JWT token
        String token = jwtUtil.generateToken(user);

        return new LoginResponse(
                token,
                user.getRole().name(),
                user.getId(),
                user.getEmail(),
                user.getFullName()
        );
    }
}
