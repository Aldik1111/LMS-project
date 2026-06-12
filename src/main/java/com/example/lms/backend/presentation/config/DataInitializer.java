package com.example.lms.backend.presentation.config;

import com.example.lms.backend.domain.entity.Role;
import com.example.lms.backend.domain.entity.User;
import com.example.lms.backend.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j // Lombok: Дает поле log для логирования
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //CommandLineRunner - run() вызывается автоматический после старта приложения
    @Override
    public void run(String... args) {
        if (!userRepository.existsByEmail("admin@lms.com")){
            User admin = User.builder()
                    .email("admin@lms.com")
                    .password(passwordEncoder.encode("admin"))
                    .fullName("Admin admin")
                    .role(Role.ADMIN)
                    .active(true)
                    .build();
            userRepository.save(admin);
            log.info("Admin created: admin@lms.com / admin");
        }
    }
}
