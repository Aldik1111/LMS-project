package com.example.lms.backend.presentation.controller;

import com.example.lms.backend.application.dto.ProctoringStatusDto;
import com.example.lms.backend.application.dto.ProctoringViolationRequest;
import com.example.lms.backend.application.dto.ProctoringPhotoRequest;
import com.example.lms.backend.application.service.ProctoringService;
import com.example.lms.backend.domain.entity.User;
import com.example.lms.backend.infrastructure.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/proctoring")
@RequiredArgsConstructor

public class ProctoringController {
    private final ProctoringService proctoringService;
    private final SecurityUtils securityUtils;

    @GetMapping("/status")
    public ResponseEntity<ProctoringStatusDto> getStatus(){
        User currentUser =  securityUtils.getCurrentUser();
        return ResponseEntity.ok(proctoringService.getStatus(currentUser.getId()));
    }

    @PostMapping("/photo")
    public ResponseEntity<Void> savePhoto(@RequestBody ProctoringPhotoRequest request){
        try {
            User currentUser =  securityUtils.getCurrentUser();
            proctoringService.savePhoto(currentUser.getId(),request);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/violation")
    public ResponseEntity<Void> logViolation(@RequestBody ProctoringViolationRequest request){
        try {
            User currentUser =  securityUtils.getCurrentUser();
            proctoringService.logViolation(currentUser.getId(),request);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
