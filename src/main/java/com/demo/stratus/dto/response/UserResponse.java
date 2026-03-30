package com.demo.stratus.dto.response;

import com.demo.stratus.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    private Role role;
    private LocalDateTime createdAt;
}