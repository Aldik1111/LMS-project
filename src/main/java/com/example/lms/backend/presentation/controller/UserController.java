package com.example.lms.backend.presentation.controller;

import com.example.lms.backend.application.dto.UserDto;
import com.example.lms.backend.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // GET /api/users
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllStudents() { // all users
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // GET /api/users/students — только студенты (для выпадашек менеджера)
    @GetMapping("/students")
    public ResponseEntity<List<UserDto>> getStudents() {
        return ResponseEntity.ok(userService.getAllStudents());
    }

    @GetMapping("/groups")
    public ResponseEntity<List<String>> getAllGroups() {
        return ResponseEntity.ok(userService.getAllGroups());
    }

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.getUserById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto dto) {
        try {
            UserDto created = userService.createUser(dto);
            return ResponseEntity.status(201).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id:\\d+}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto dto) {
        try {
            return ResponseEntity.ok(userService.updateUser(id, dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}