package com.example.lms.backend.domain.repository;

import com.example.lms.backend.domain.entity.Assignment;
import com.example.lms.backend.domain.entity.AssignmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment,Long> {
    List<Assignment> findAllByStudentId (Long studentId);

    List<Assignment> findAllByStudentIdAndStatus (Long studentId, AssignmentStatus assignmentStatus);
}
