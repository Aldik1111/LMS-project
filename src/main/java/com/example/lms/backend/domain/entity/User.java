package com.example.lms.backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class User {
    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto increament
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password; // save BCrypt hash

    @Column(nullable = false)
    private String fullName;

    @Enumerated(EnumType.STRING) // save as "ADMIN/Manager/Student"
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private boolean active = true;
}
