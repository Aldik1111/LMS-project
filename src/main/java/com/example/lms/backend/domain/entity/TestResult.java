package com.example.lms.backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "test_ressult")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class TestResult {
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
    private LocalDateTime completedAt;

    private Integer score;
    private Integer totalPoints;

    @OneToMany(mappedBy = "testResult", cascade = CascadeType.ALL)
    @Builder.Default
    private List<StudentAnswer> StudentAnswer = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.completedAt = LocalDateTime.now();
    }
}
