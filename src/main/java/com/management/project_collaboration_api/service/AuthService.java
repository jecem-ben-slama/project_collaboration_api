package com.management.project_collaboration_api.service;

import com.management.project_collaboration_api.dto.AuthResponse;
import com.management.project_collaboration_api.dto.LoginRequest;
import com.management.project_collaboration_api.model.User;
import com.management.project_collaboration_api.repository.UserRepository;
import com.management.project_collaboration_api.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;

    public AuthResponse authenticate(LoginRequest request) {
        // 1. Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // 2. Check if the raw password matches the hashed password in DB
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // 3. Generate the JWT
        String token = jwtUtils.generateToken(user.getEmail(), user.getRole().name(), user.getId());

        // 4. Return the response containing the token
        return new AuthResponse(token, user.getEmail(), user.getRole().name());
    }
}