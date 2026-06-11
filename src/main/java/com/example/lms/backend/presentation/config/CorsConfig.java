package com.example.lms.backend.presentation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // Разрешаем запросы с фронта
        // Когда фронт задеплоен — добавить его URL сюда
        config.setAllowedOrigins(List.of(
                "http://localhost:3000",  // React dev server
                "http://localhost:5173",  // Vite dev server
                "http://localhost:4200",  // Angular dev server
                "http://127.0.0.1:5500"  // Live Server (VS Code)
        ));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Рвзрешенные HTTP методы

        config.setAllowCredentials(true); // Разрешаем отправку куки и заголовков авторизации

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config); // приминяем ко всем эндпойнтам

        return new CorsFilter(source);
    }

}
