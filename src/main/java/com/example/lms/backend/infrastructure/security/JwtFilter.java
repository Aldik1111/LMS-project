package com.example.lms.backend.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    // OncePerRequestFilter - фильтр сработает тока 1 раз

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, // входящий запрос
            HttpServletResponse response, // исходящий ответ
            FilterChain filterChain // цепочка след фильтров
    ) throws ServletException, IOException{

        // достаем заголовок Authorization: "Bearer sdfjSAs...."
        String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization = " + authHeader); ///////////

        // если заголовка нет или не подходит - пропуск без авторизаций
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // remove "Bearer " (7 symbols)
        String token = authHeader.substring(7);
        System.out.println("Token = " + token);//////////

        if (!jwtUtil.isTokenValid(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // get email from token
        String email = jwtUtil.extractEmail(token);
        System.out.println("Email from token = " + email);/////////

        // if user not auth
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Load User from DB
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // create object authentication
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, // кто авторизовани
                    null,  // пароль (уже проверен, не нужен(
                    userDetails.getAuthorities() // Роли
            );

            // 9. Говорим Spring Security: этот юзер авторизован
            // Теперь в любом контроллере можно получить текущего юзера
            System.out.println("User authenticated: " + userDetails.getUsername()); ////////
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        // Передаем запрос дальше по цепочке фильтров
        filterChain.doFilter(request, response);
    }
}
