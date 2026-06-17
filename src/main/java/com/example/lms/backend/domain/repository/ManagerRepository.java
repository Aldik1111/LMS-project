package com.example.lms.backend.domain.repository;

import com.example.lms.backend.domain.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends JpaRepository<Student, Long> { // <Which entity, primary key>
}
