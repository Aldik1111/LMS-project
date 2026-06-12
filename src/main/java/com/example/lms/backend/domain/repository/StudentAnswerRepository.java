package com.example.lms.backend.domain.repository;

import com.example.lms.backend.domain.entity.StudentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, Long> {
    List<StudentAnswer> findAllByTestResultId(Long testResultId);
}
