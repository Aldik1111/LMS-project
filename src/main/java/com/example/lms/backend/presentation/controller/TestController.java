package com.example.lms.backend.presentation.controller;

import com.example.lms.backend.application.dto.TestDto;
import com.example.lms.backend.application.dto.TestResultDto;
import com.example.lms.backend.application.dto.TestSubmitRequest;
import com.example.lms.backend.application.service.TestService;
import com.example.lms.backend.domain.entity.Student;
import com.example.lms.backend.domain.entity.User;
import com.example.lms.backend.infrastructure.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tests")
@RequiredArgsConstructor
public class TestController {
    private final TestService testService;
    private final SecurityUtils securityUtils;

    // GET /api/tests
    @GetMapping
    public ResponseEntity<List<TestDto>> getAllTests(){
        return ResponseEntity.ok(testService.getAllTests());
    }

    //GET /api/tests/{id}
    @GetMapping("/{id}")
    public ResponseEntity<TestDto> getTestById(@PathVariable Long id){
        try {
            User currentUser = securityUtils.getCurrentUser();
            boolean isStudent = currentUser instanceof Student;
            return ResponseEntity.ok(testService.getTestById(id, isStudent));
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    // POST /api/tests only for manager
    @PostMapping
    public ResponseEntity<?> createTest(@RequestBody TestDto dto){
        try {
            User currentUser = securityUtils.getCurrentUser();
            TestDto created = testService.createTest(dto, currentUser.getId());
            return ResponseEntity.status(201).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE /api/tests/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTest(@PathVariable Long id){
        try{
            testService.deleteTest(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // POST /api/tests/{id}/submit
    @PostMapping("/{id}/submit")
    public ResponseEntity<?> submitTest(
            @PathVariable Long id,
            @RequestBody TestSubmitRequest request) {
        try {
            User currentUser = securityUtils.getCurrentUser();
            request.setTestId(id);
            TestResultDto result = testService.submitTest(request, currentUser.getId());
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
