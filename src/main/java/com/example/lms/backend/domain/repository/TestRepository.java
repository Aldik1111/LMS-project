package com.example.lms.backend.domain.repository;

import com.example.lms.backend.domain.entity.Test;
import com.example.lms.backend.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {
    List<Test> findALlByCreatedBy(User createdBy);

    // Шаг 1: только тест + вопросы (одна коллекция — безопасно для JOIN FETCH)
    @Query("SELECT DISTINCT t FROM Test t " +
            "LEFT JOIN FETCH t.questions " +
            "WHERE t.id = :id")
    Optional<Test> findByIdWithQuestions(@Param("id") Long id);
}