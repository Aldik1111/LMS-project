package com.example.lms.backend.application.service;

import com.example.lms.backend.application.dto.TestResultDto;
import com.example.lms.backend.domain.entity.TestResult;
import com.example.lms.backend.domain.repository.TestRepository;
import com.example.lms.backend.domain.repository.TestResultRepository;
import com.example.lms.backend.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final TestResultRepository testResultRepository;
    private final UserRepository userRepository;
    private final TestRepository testRepository;

    public List<TestResultDto> getResultsByTest(Long testId) { // Stata by test
        return testResultRepository.findAllByTestIDwithDetails(testId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<TestResultDto> getResultsByStudent(Long studentId) { // Stata by student
        return testResultRepository.findAllByStudentIdWithDetails(studentId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<TestSummaryDto> getOverallStatistics(){ // summary statistics
        return testRepository.findAll()
                .stream()
                .map(test -> {
                    List<TestResult> results =
                            testResultRepository.findAllByTestIDwithDetails(test.getId());

                    double avgScore = results.isEmpty() ? 0 : // average score test
                            results.stream()
                                    .mapToInt(TestResult::getScore)
                                    .average()
                                    .orElse(0);

                    double avgPercent = results.isEmpty() ? 0 :
                            results.stream()
                                    .mapToDouble(r ->
                                            r.getTotalPoints() > 0
                                            ? (double) r.getScore() / r.getTotalPoints() * 100 : 0
                                    )
                                    .average()
                                    .orElse(0);

                    return new TestSummaryDto(
                            test.getId(),
                            test.getTitle(),
                            results.size(),
                            avgScore,
                            avgPercent
                    );
                })
                .collect(Collectors.toList());
    }

    private TestResultDto toDto(TestResult r) {
        int attemptNumber = 0;

        if(r.getAssignment() != null){
            attemptNumber = (int) testResultRepository.findAllByStudentId(r.getStudent().getId())
                    .stream()
                    .filter(other -> other.getAssignment() != null
                            && other.getAssignment().getId().equals(r.getAssignment().getId())
                            && !other.getCompletedAt().isAfter(r.getCompletedAt()))
                    .count();
        }

        return new TestResultDto(
                r.getId(),
                r.getTest().getTitle(),
                r.getStudent().getFullName(),
                r.getScore(),
                r.getTotalPoints(),
                r.getTotalPoints() - r.getScore(),
                r.getCompletedAt(),
                null,
                attemptNumber
        );
    }

    @Data
    @AllArgsConstructor
    public static class TestSummaryDto {
        private Long testId;
        private String testTitle;
        private Integer attemptCount;
        private Double averageScore;
        private Double averagePercent;
    }

}