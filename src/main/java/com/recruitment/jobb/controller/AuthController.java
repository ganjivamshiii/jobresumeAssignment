package com.recruitment.jobb.controller;

import com.recruitment.jobb.dto.ApiResponse;
import com.recruitment.jobb.dto.JwtResponse;
import com.recruitment.jobb.dto.LoginRequest;
import com.recruitment.jobb.dto.SignupRequest;
import com.recruitment.jobb.model.User;
import com.recruitment.jobb.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signup(@Valid @RequestBody SignupRequest signupRequest) {
        User user = authService.signup(signupRequest);
        return ResponseEntity.ok(new ApiResponse(true, "User registered successfully", user));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = authService.login(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }
}