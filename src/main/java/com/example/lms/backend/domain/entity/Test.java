package com.example.lms.backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "tests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY) // не хагружать юзера пока не над
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany( // One test - many questions
            mappedBy = "test", // связь описана в class question
            cascade = CascadeType.ALL, // 1 operation - all tests
            orphanRemoval = true) // remove from list - remove from DB
    @Builder.Default
    private List<Question> questions = new ArrayList<>();

    @PrePersist
    public void prePersist() { // call hibernate before save in DB
        this.createdAt = LocalDateTime.now();
    }
}
