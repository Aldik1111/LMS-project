package com.example.lms.backend.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class QuestionDto {
    private Long id;
    private String label;
    private String answerText;
    private Boolean correct;
}
