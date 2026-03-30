package com.demo.stratus.security;

import com.demo.stratus.dto.request.LoginRequest;
import com.demo.stratus.dto.request.RegisterRequest;
import com.demo.stratus.dto.response.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Register, login and refresh token endpoints")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Register request received for: {}", request.getEmail());
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, response.getTokenType() + " " + response.getAccessToken())
                .body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Login with email and password")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login request received for: {}", request.getEmail());
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, response.getTokenType() + " " + response.getAccessToken())
                .body(response);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token using refresh token")
    public ResponseEntity<AuthResponse> refresh(@RequestHeader("Refresh-Token") String refreshToken) {
        log.info("Refresh token request received");
        AuthResponse response = authService.refresh(refreshToken);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, response.getTokenType() + " " + response.getAccessToken())
                .body(response);
    }
}