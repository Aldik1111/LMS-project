package com.example.lms.backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "proctoring_photos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ProctoringPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User student;

    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    private String photoBase64;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void PrePersist(){
        this.createdAt = LocalDateTime.now();
    }
}
