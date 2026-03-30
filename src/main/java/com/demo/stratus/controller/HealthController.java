package com.demo.stratus.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@Tag(name = "System", description = "Health and Connectivity checks")
public class HealthController {
    @Operation(summary = "Check if Stratus is live", description = "Returns a welcome message.")
    @GetMapping("/hello")
    public String sayHello() {
        return "Welcome to Stratus! System is cleared for takeoff. 🛫";
    }
}
