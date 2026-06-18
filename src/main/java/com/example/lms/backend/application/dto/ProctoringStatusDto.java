package com.example.lms.backend.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProctoringStatusDto {
    private boolean hasReferencePhoto; // есть ли уже сохр фото
}
