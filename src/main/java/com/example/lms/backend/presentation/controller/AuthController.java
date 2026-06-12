package com.example.lms.backend.presentation.controller;


import com.example.lms.backend.application.dto.LoginRequest;
import com.example.lms.backend.application.dto.LoginResponse;
import com.example.lms.backend.application.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

   @PostMapping("/login")
   public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
       try {
           LoginResponse response = authService.login(request);
           return ResponseEntity.ok(response);
       } catch (RuntimeException e) {
           return ResponseEntity.status(401).build();
       }
   }
}

