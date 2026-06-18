package com.example.lms.backend.domain.repository;

import com.example.lms.backend.domain.entity.ProctoringPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProctoringPhotoRepository extends JpaRepository<ProctoringPhoto, Long>{
    Optional<ProctoringPhoto> findAllByStudentId(Long studentId);
    boolean existsByStudentId(Long studentId);
}
