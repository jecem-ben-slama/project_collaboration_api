package com.management.project_collaboration_api.controller;

import com.management.project_collaboration_api.dto.AuthResponse;
import com.management.project_collaboration_api.dto.LoginRequest;
import com.management.project_collaboration_api.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth") // This matches the permitAll() in WebSecurityConfig
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.authenticate(request);
    }
}