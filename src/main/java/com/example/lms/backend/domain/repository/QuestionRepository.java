package com.example.lms.backend.domain.repository;

import com.example.lms.backend.domain.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> {
    List<Question> findAllByTestId(Long testId); // select * from questiong where test_id = ? order by order_index asc

    long countByTestId(Long testId);

    @Query("SELECT DISTINCT q FROM Question q " +
            "LEFT JOIN FETCH q.answers " +
            "WHERE q.test.id = :testId")
    List<Question> findByTestIdWithAnswers(@Param("testId") Long testId);
}

