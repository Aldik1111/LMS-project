package com.example.lms.backend.application.dto;

import com.example.lms.backend.domain.entity.StudentAnswer;
import lombok.Data;

import java.util.List;

@Data

public class TestSubmitRequest {
    private Long testId;
    private List<StudentAnswerDto> answers;

    @Data
    public static class StudentAnswerDto {
        private Long questionId;
        private Long selectedAnswerId;
    }
}
