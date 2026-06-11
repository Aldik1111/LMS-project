package com.example.lms.backend.domain.repository;

import com.example.lms.backend.domain.entity.Role;
import com.example.lms.backend.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> { // <Which entity, primary key>
    Optional<User> findByEmail(String email); // select * from users WHERE email = ?
        // `contain` user or empty. for NullPointerException error

    List<User> findAllByRole(Role role); // select * from users WHERE role = ?

    boolean existsByEmail(String email); // select EXISTS ( select from users WHERE email = ? )

}
