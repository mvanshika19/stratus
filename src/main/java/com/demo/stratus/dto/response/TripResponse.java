package com.demo.stratus.dto.response;

import com.demo.stratus.enums.TripPurpose;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TripResponse {
    private Long id;
    private String name;
    private TripPurpose purpose;
    private Integer accompaniedMembers;
    private Long userId;
    private LocalDateTime createdAt;
}