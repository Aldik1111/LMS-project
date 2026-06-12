package com.example.lms.backend.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class QuestionDto {
    private Long id;
    private String questionText;
    private Integer orderIndex;
    private List<AnswerDto> answers;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AnswerDto {
        private Long id;
        private String label;
        private String answerText;
        private Boolean correct;
    }
}
