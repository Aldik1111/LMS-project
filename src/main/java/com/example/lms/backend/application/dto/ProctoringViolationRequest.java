package com.example.lms.backend.application.dto;

import lombok.Data;

@Data
public class ProctoringViolationRequest {
    private Long testId;
    private String violationType;
}
