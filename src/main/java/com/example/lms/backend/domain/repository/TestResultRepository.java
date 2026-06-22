package com.example.lms.backend.domain.repository;

import com.example.lms.backend.domain.entity.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestResultRepository extends JpaRepository<TestResult,Long> {
    List<TestResult> findAllByTestId (Long testId);

    List<TestResult> findAllByStudentId (Long studentId);

    boolean existsByStudentIdAndTestId (Long studentId, Long testId);
    boolean existsByStudentIdAndAssignmentId (Long studentId, Long assignmentId);

    int countByStudentIdAndAssignmentId(Long studentId, Long assignmentId);

    Optional<TestResult> findByStudentIdAndAssignmentId (Long studentId, Long assignmentId);

    @Query("SELECT AVG(tr.score) FROM TestResult tr WHERE tr.test.id = :testId")
    Double findAverageScoreByTestId (@Param("testId") Long testId);

    @Query("SELECT MAX(tr.score) FROM TestResult tr WHERE tr.assignment.id = :assignmentId AND tr.student.id = :studentId")
    Integer findBestScoreByStudentIdAndAssignmentId(@Param("studentId") Long studentId, @Param("assignmentId") Long assignmentId);

    @Query("SELECT tr FROM TestResult tr " +
            "JOIN FETCH tr.student " +
            "JOIN FETCH tr.test " +
            "WHERE tr.test.id = :testId")
    List<TestResult> findAllByTestIDwithDetails(@Param("testId") Long testId);

    @Query("SELECT tr FROM TestResult tr " +
            "JOIN FETCH tr.student " +
            "JOIN FETCH tr.test " +
            "WHERE tr.student.id = :studentId")
    List<TestResult> findAllByStudentIdWithDetails(@Param("studentId") Long studentId);

    @Query("SELECT tr FROM TestResult tr WHERE tr.student.id = :studentId AND tr.test.id = :testId ORDER BY tr.completedAt DESC")
    List<TestResult> findAllByStudentIdAndTestIdOrderByCompletedAtDesc(@Param("studentId") Long studentId, @Param("testId") Long testId);
}