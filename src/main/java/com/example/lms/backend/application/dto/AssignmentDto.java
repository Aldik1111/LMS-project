package com.example.lms.backend.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor


public class AssignmentDto {
    private Long id;
    private String title;
    private String description;
    private String targetGroup;
    private Long testId;
    private String TestTitle;
    private LocalDateTime createdAt;
    private LocalDateTime deadline;
    private Integer score;
    private Integer totalPoints;
    private Boolean completed;
    private Boolean expired;
}
