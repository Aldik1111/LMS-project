package com.example.lms.backend.infrastructure.security;

import com.example.lms.backend.domain.entity.User;
import com.example.lms.backend.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor    // Lombok: конструктор для всех final полей
                            // это лучший способ внедрения зависимостей
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    // Spring Security вызывает этот метод когда нужно загрузить юзера
    // "username" здесь — это наш email
    public UserDetails loadUserByUsername(String email)
        throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        // Преобразуем нашего User в UserDetails который понимает Spring Security
        // SimpleGrantedAuthority — это роль в формате Spring Security
        // "ROLE_" префикс обязателен для Spring Security
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())) // example: ROLE_ADMIN
        );
    }
}
