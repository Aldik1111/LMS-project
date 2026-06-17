package com.example.lms.backend.presentation.controller;


import com.example.lms.backend.application.dto.AssignmentDto;
import com.example.lms.backend.application.service.AssignmentService;
import com.example.lms.backend.domain.entity.Student;
import com.example.lms.backend.domain.entity.User;
import com.example.lms.backend.infrastructure.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
public class AssignmentController {
    private final AssignmentService assignmentService;
    private final SecurityUtils securityUtils;

    // GET /api/assignments
    @GetMapping
    public ResponseEntity<List<AssignmentDto>> getAssignments() {
        User currentUser = securityUtils.getCurrentUser();

        if (currentUser instanceof Student) {
            return ResponseEntity.ok(
                    assignmentService.getAssignmentsForStudents(currentUser.getId())
            );
        }

        return ResponseEntity.ok(assignmentService.getAllAssignments());
    }

    // POST /api/assignemtns
    @PostMapping
    public ResponseEntity<AssignmentDto> createAssignment(@RequestBody AssignmentDto dto) {
        try {
            User currentUser = securityUtils.getCurrentUser();
            AssignmentDto created = assignmentService.createAssignment(dto, currentUser.getId());
            return ResponseEntity.status(201).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // DELETE /api/assignments/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        try {
            assignmentService.deleteAssignment(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // PUT /api/assignments/{id}/complete
    @PutMapping("/{id}/complete")
    public ResponseEntity<AssignmentDto> completeAssignment(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(assignmentService.completeAssignment(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
