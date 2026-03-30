package com.demo.stratus.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TripResponse {
    private Long id;
    private String name;
    private Long userId;
    private LocalDateTime createdAt;
}