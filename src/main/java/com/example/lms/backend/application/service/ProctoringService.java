package com.example.lms.backend.application.service;

import com.example.lms.backend.application.dto.ProctoringPhotoRequest;
import com.example.lms.backend.application.dto.ProctoringStatusDto;
import com.example.lms.backend.application.dto.ProctoringViolationRequest;
import com.example.lms.backend.domain.entity.ProctoringPhoto;
import com.example.lms.backend.domain.entity.ProctoringViolation;
import com.example.lms.backend.domain.entity.Test;
import com.example.lms.backend.domain.entity.User;
import com.example.lms.backend.domain.repository.ProctoringPhotoRepository;
import com.example.lms.backend.domain.repository.ProctoringViolationRepository;
import com.example.lms.backend.domain.repository.TestRepository;
import com.example.lms.backend.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class ProctoringService {
    private final ProctoringViolationRepository violationRepository;
    private final ProctoringPhotoRepository photoRepository;
    private final UserRepository userRepository;
    private final TestRepository testRepository;

    public ProctoringStatusDto getStatus(Long studentId) { // проверка есть ли уже фото
        boolean hasPhoto = photoRepository.existsByStudentId(studentId);
        return new ProctoringStatusDto(hasPhoto);
    }


    public void savePhoto(Long studentId, ProctoringPhotoRequest request) { // сохр фото стуендта
        User student = userRepository.findById(studentId).
                orElseThrow(() -> new RuntimeException("User not found"));


        // Если уже есть - оставляем
        if (photoRepository.existsByStudentId(studentId)) {
            return;
        }

        ProctoringPhoto photo = ProctoringPhoto.builder()
                .student(student)
                .photoBase64(request.getPhotoBase64())
                .build();
        photoRepository.save(photo);
    }

    public void logViolation(Long studentId, ProctoringViolationRequest request) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Test test = testRepository.findById(request.getTestId())
                .orElseThrow(() -> new RuntimeException("Test not found"));

        ProctoringViolation violation = ProctoringViolation.builder()
                .student(student)
                .test(test)
                .violationType(request.getViolationType())
                .build();

        violationRepository.save(violation);

    }

}

