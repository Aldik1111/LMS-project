package com.example.lms.backend.application.service;

import com.example.lms.backend.application.dto.UserDto;
import com.example.lms.backend.domain.entity.Role;
import com.example.lms.backend.domain.entity.User;
import com.example.lms.backend.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<UserDto> getAllStudents() {
        return userRepository.findAllByRole(Role.STUDENT)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public UserDto createUser(UserDto dto) {
        if(userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword())) // Hash password
                .fullName(dto.getFullname())
                .role(dto.getRole())
                .active(dto.getActive() != null ? dto.getActive() : true)
                .build();

        User saved = userRepository.save(user);
        return toDto(saved);
    }

    public UserDto updateUser(Long id, UserDto dto) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        user.setFullName(dto.getFullname());
        user.setRole(dto.getRole());
        user.setActive(dto.getActive() != null ? dto.getActive() : true);

        if(dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        User saved = userRepository.save(user);
        return toDto(saved);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return toDto(user);
    }

    private UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                null, // password
                user.getFullName(),
                user.getRole(),
                user.isActive()
        );
    }
}
