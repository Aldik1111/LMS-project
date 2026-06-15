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

        @Query("SELECT DISTINCT t FROM Test t " +
                "LEFT JOIN FETCH t.questions q " +
                "LEFT JOIN FETCH q.answers " +
                "WHERE t.id = :id")
        Optional<Test> findbyIDWithQAndA(@Param("id") Long id); // найти тест по ид + загрузить вопросы и ответы сразу
    }
