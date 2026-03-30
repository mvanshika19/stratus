package com.demo.stratus.controller;

import com.demo.stratus.dto.request.UpdateProfileRequest;
import com.demo.stratus.dto.response.ProfileResponse;
import com.demo.stratus.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
@Tag(name = "Profile", description = "User profile management")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get current user profile")
    public ResponseEntity<ProfileResponse> getProfile(
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("Get profile request for: {}", userDetails.getUsername());
        return ResponseEntity.ok(userService.getProfile(userDetails.getUsername()));
    }

    @PatchMapping
    @Operation(summary = "Update current user profile")
    public ResponseEntity<ProfileResponse> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UpdateProfileRequest request) {
        log.info("Update profile request for: {}", userDetails.getUsername());
        return ResponseEntity.ok(userService.updateProfile(userDetails.getUsername(), request));
    }
}