package com.example.lms.backend.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class TestDto {
    private Long id;
    private String title;
    private String description;
    private String createdByName;
    private LocalDateTime createdAt;
    private List<QuestionDto> questions;
}
