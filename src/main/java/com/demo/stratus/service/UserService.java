package com.demo.stratus.service;

import com.demo.stratus.dto.request.UpdateProfileRequest;
import com.demo.stratus.dto.response.ProfileResponse;
import com.demo.stratus.exception.UserNotFoundException;
import com.demo.stratus.model.User;
import com.demo.stratus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public ProfileResponse getProfile(String email) {
        log.info("Fetching profile for: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        return mapToProfileResponse(user);
    }

    @Transactional
    public ProfileResponse updateProfile(String email, UpdateProfileRequest request) {
        log.info("Updating profile for: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());
        if (request.getDateOfBirth() != null) user.setDateOfBirth(request.getDateOfBirth());
        if (request.getStreet() != null) user.setStreet(request.getStreet());
        if (request.getCity() != null) user.setCity(request.getCity());
        if (request.getProvince() != null) user.setProvince(request.getProvince());
        if (request.getPostalCode() != null) user.setPostalCode(request.getPostalCode());
        if (request.getCountry() != null) user.setCountry(request.getCountry());
        if (request.getProfilePictureUrl() != null) user.setProfilePictureUrl(request.getProfilePictureUrl());

        userRepository.save(user);
        log.info("Profile updated successfully for: {}", email);
        return mapToProfileResponse(user);
    }

    private ProfileResponse mapToProfileResponse(User user) {
        return new ProfileResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                user.getPhoneNumber(),
                user.getDateOfBirth(),
                user.getStreet(),
                user.getCity(),
                user.getProvince(),
                user.getPostalCode(),
                user.getCountry(),
                user.getProfilePictureUrl(),
                user.getCreatedAt()
        );
    }
}