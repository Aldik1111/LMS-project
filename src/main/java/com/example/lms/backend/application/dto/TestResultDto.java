package com.example.lms.backend.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private int attemptNumber;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AnswerDetailDto {
        private String questionText;
        private String selectedAnswer;
        private String correctAnswer;

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private boolean correct;

    }

}
