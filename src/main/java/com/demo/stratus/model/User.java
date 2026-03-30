package com.demo.stratus.model;

import com.demo.stratus.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "phone_number")
private String phoneNumber;

@Column(name = "date_of_birth")
private LocalDate dateOfBirth;

@Column(name = "street")
private String street;

@Column(name = "city")
private String city;

@Column(name = "province")
private String province;

@Column(name = "postal_code")
private String postalCode;

@Column(name = "country")
private String country;

@Column(name = "profile_picture_url")
private String profilePictureUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Trip> trips;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}