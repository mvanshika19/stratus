package com.demo.stratus.security;

import com.demo.stratus.dto.request.LoginRequest;
import com.demo.stratus.dto.request.RegisterRequest;
import com.demo.stratus.dto.response.AuthResponse;
import com.demo.stratus.enums.Role;
import com.demo.stratus.exception.EmailAlreadyExistsException;
import com.demo.stratus.exception.InvalidTokenException;
import com.demo.stratus.exception.UserNotFoundException;
import com.demo.stratus.model.User;
import com.demo.stratus.repository.UserRepository;
import com.demo.stratus.service.EmailTemplateService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailTemplateService emailTemplateService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Registration failed - email already exists: {}", request.getEmail());
            throw new EmailAlreadyExistsException(request.getEmail()); // ✅
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.STANDARD_USER);

        userRepository.save(user);
        log.info("User registered successfully: {}", request.getEmail());

        emailTemplateService.sendWelcomeEmail(request.getEmail(), request.getFirstName());

        String accessToken = jwtService.generateAccessToken(user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        return buildAuthResponse(accessToken, refreshToken);
    }

    public AuthResponse login(LoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException(request.getEmail()));

        String accessToken = jwtService.generateAccessToken(user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        log.info("Login successful for email: {}", request.getEmail());
        return buildAuthResponse(accessToken, refreshToken);
    }

    public AuthResponse refresh(String refreshToken) {
        log.info("Processing refresh token request");

        if (!jwtService.isRefreshToken(refreshToken)) {
            throw new InvalidTokenException("Token is not a refresh token"); // ✅
        }

        String email = jwtService.extractEmail(refreshToken);

        if (!jwtService.isTokenValid(refreshToken, email)) {
            throw new InvalidTokenException("Refresh token expired or invalid"); // ✅
        }

        userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        String newAccessToken = jwtService.generateAccessToken(email);
        String newRefreshToken = jwtService.generateRefreshToken(email);

        log.info("Tokens refreshed successfully for: {}", email);
        return buildAuthResponse(newAccessToken, newRefreshToken);
    }

    private AuthResponse buildAuthResponse(String accessToken, String refreshToken) {
        return new AuthResponse(accessToken, refreshToken, "Bearer");
    }
}