package com.example.lms.backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "proctoring_violations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ProctoringViolation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id")
    private Test test;

    @Column(nullable = false)
    private String violationType;

    @Column(nullable = false)
    private LocalDateTime occuredAt;

    @PrePersist
    private void prePersist(){
        this.occuredAt = LocalDateTime.now();
    }
}
