package com.example.lms.backend.application.service;

import com.example.lms.backend.application.dto.AssignmentDto;
import com.example.lms.backend.domain.entity.Assignment;
import com.example.lms.backend.domain.entity.AssignmentStatus;
import com.example.lms.backend.domain.entity.Test;
import com.example.lms.backend.domain.entity.TestResult;
import com.example.lms.backend.domain.entity.User;
import com.example.lms.backend.domain.repository.AssignmentRepository;
import com.example.lms.backend.domain.repository.TestRepository;
import com.example.lms.backend.domain.repository.TestResultRepository;
import com.example.lms.backend.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor()

public class AssignmentService {
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;
    private final TestRepository testRepository;
    private final TestResultRepository testResultRepository;

    @Transactional(readOnly = true)
    public List<AssignmentDto> getAllAssignments() { // For manager
        return assignmentRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AssignmentDto> getAssignmentsForStudents(Long studentId){ // For students
        List<Assignment> assignments = assignmentRepository.findAllByStudentId(studentId);
        List<TestResult> results = testResultRepository.findAllByStudentId(studentId);
        return assignments.stream().map(a -> {
            AssignmentDto dto = toDto(a);
            if (a.getTest() != null) {
                results.stream()
                    .filter(r -> r.getTest().getId().equals(a.getTest().getId()))
                    .findFirst()
                    .ifPresent(r -> {
                        dto.setScore(r.getScore());
                        dto.setTotalPoints(r.getTotalPoints());
                    });
            }
            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public AssignmentDto createAssignment(AssignmentDto dto, Long creatorId) {
        User student = userRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("Creator not found"));

        Test test = null;
        if (dto.getTestId() != null) {
            test = testRepository.findById(dto.getTestId())
                    .orElseThrow(() -> new RuntimeException("Test not found"));
        }

        Assignment assignment = Assignment.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .student(student)
                .test(test)
                .assignedBy(creator)
                .deadline(dto.getDeadline())
                .status(AssignmentStatus.PENDING)
                .build();

        Assignment saved =  assignmentRepository.save(assignment);
        return toDto(saved);
    }

    @Transactional
    public AssignmentDto completeAssignment(Long assignmentId){
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        assignment.setStatus(AssignmentStatus.COMPLETED);
        Assignment saved = assignmentRepository.save(assignment);
        return toDto(saved);
    }

    public void deleteAssignment(Long id) {
        if (!assignmentRepository.existsById(id)) {
            throw new RuntimeException("Assignment not found");
        }
        assignmentRepository.deleteById(id);
    }

    private AssignmentDto toDto(Assignment a) {
        return new AssignmentDto(
                a.getId(),
                a.getTitle(),
                a.getDescription(),
                a.getStudent() != null ? a.getStudent().getId() : null,
                a.getStudent() != null ? a.getStudent().getFullName() : "-",
                a.getTest() != null ? a.getTest().getId() : null,
                a.getTest() != null ? a.getTest().getTitle() : "-",
                a.getStatus(),
                a.getCreatedAt(),
                a.getDeadline(),
                null,
                null
        );
    }

}
