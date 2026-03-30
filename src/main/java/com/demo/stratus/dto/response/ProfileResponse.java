package com.demo.stratus.dto.response;

import com.demo.stratus.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ProfileResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String street;
    private String city;
    private String province;
    private String postalCode;
    private String country;
    private String profilePictureUrl;
    private LocalDateTime createdAt;
}