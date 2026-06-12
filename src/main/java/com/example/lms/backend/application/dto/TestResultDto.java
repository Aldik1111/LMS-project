package com.example.lms.backend.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class TestResultDto {
    private Long id;
    private String testTitle;
    private String studentName;
    private Integer score;
    private Integer totalPoints;
    private Integer incorrectAnswers;
    private LocalDateTime completedAt;
    private List<AnswerDetailDto> details;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AnswerDetailDto {
        private String questionText;
        private String selectedAnswer;
        private String correctAnswer;
        private boolean correct;
    }

}
