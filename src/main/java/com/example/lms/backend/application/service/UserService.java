package com.example.lms.backend.application.service;

import com.example.lms.backend.application.dto.UserDto;
import com.example.lms.backend.domain.entity.*;
import com.example.lms.backend.domain.repository.StudentRepository;
import com.example.lms.backend.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class UserService {
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<UserDto> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public UserDto createUser(UserDto dto) {
        if(userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        boolean isActive = dto.getActive() != null ? dto.getActive() : true;

        // Полиморфизм в действии: создаём РАЗНЫЙ объект в зависимости от роли,
        // но дальше работаем с ним через общий интерфейс User


        User user = switch (dto.getRole()) {
            case "ADMIN" -> Admin.builder()
                    .email(dto.getEmail())
                    .password(encodedPassword)
                    .fullName(dto.getFullName())
                    .active(isActive)
                    .department("General")
                    .build();

            case "MANAGER" -> Manager.builder()
                    .email(dto.getEmail())
                    .password(encodedPassword)
                    .fullName(dto.getFullName())
                    .active(isActive)
                    .managedDepartment("General")
                    .build();
            case "STUDENT" -> Student.builder()
                    .email(dto.getEmail())
                    .password(encodedPassword)
                    .fullName(dto.getFullName())
                    .active(isActive)
                    .groupNumber(dto.getGroup() != null ? dto.getGroup() : "N/A")
                    .build();
            default -> throw new RuntimeException("Invalid role: " + dto.getRole());
        };

        User saved = userRepository.save(user);
        return toDto(saved);
    }

    public UserDto updateUser(Long id, UserDto dto) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        user.setFullName(dto.getFullName());
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

    public List<String> getAllGroups() {
        return studentRepository.findAll()
                .stream()
                .map(Student::getGroupNumber)
                .filter(g -> g != null && !g.isBlank())
                .distinct()
                .sorted()
                .collect(Collectors.toUnmodifiableList());
    }

    private UserDto toDto(User user) {
        String group = (user instanceof Student s) ? s.getGroupNumber() : null;
        return new UserDto(
                user.getId(),
                user.getEmail(),
                null, // password
                user.getFullName(),
                RoleResolve.resolve(user),
                user.isActive(),
                group
        );
    }
}
