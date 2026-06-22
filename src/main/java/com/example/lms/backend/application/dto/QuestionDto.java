package com.example.lms.backend.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private Boolean correct;
    }
}
