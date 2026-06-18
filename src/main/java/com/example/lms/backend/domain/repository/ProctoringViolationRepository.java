package com.example.lms.backend.domain.repository;

import com.example.lms.backend.domain.entity.ProctoringViolation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProctoringViolationRepository extends JpaRepository<ProctoringViolation, Long>{
    List<ProctoringViolation> findAllByStudentId(Long studentId);
    List<ProctoringViolation> findAllByTestId(Long testId);
}
