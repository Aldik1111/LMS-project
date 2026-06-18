package com.example.lms.backend.application.service;

import com.example.lms.backend.application.dto.AssignmentDto;
import com.example.lms.backend.domain.entity.Assignment;
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

import java.time.LocalDateTime;
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
                .map(a -> toDto(a,null))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AssignmentDto> getAssignmentsForGroup(String group, Long studentId){ // For students
        List<Assignment> assignments = assignmentRepository.findAllByTargetGroup(group);
        List<TestResult> results = testResultRepository.findAllByStudentId(studentId);

        return assignments.stream().map(a -> {
            AssignmentDto dto = toDto(a, studentId);

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
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("Creator not found"));

        if (dto.getTargetGroup() == null || dto.getTargetGroup().isBlank()) {
            throw new RuntimeException("Target group is required");
        }

        Test test = null;
        if (dto.getTestId() != null) {
            test = testRepository.findById(dto.getTestId())
                    .orElseThrow(() -> new RuntimeException("Test not found"));
        }

        Assignment assignment = Assignment.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .targetGroup(dto.getTargetGroup())
                .test(test)
                .assignedBy(creator)
                .deadline(dto.getDeadline())
                .build();

        Assignment saved =  assignmentRepository.save(assignment);
        return toDto(saved, null);
    }

    public void deleteAssignment(Long id) {
        if (!assignmentRepository.existsById(id)) {
            throw new RuntimeException("Assignment not found");
        }
        assignmentRepository.deleteById(id);
    }

    private AssignmentDto toDto(Assignment a, Long studentId) {
        boolean completed = false;

        if (studentId != null && a.getTest() != null) {
            completed = testResultRepository.existsByStudentIdAndTestId(studentId, a.getTest().getId());
        }

        boolean expired = a.getDeadline() != null && a.getDeadline().isBefore(LocalDateTime.now());

        return new AssignmentDto(
                a.getId(),
                a.getTitle(),
                a.getDescription(),
                a.getTargetGroup(),
                a.getTest() != null ? a.getTest().getId() : null,
                a.getTest() != null ? a.getTest().getTitle() : "-",
                a.getCreatedAt(),
                a.getDeadline(),
                null,
                null,
                completed,
                expired
        );
    }

}
