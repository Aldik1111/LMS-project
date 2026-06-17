package com.example.lms.backend.infrastructure.security;

import com.example.lms.backend.domain.entity.User;
import com.example.lms.backend.domain.entity.RoleResolve;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component  // говорит Spring — создай один экземпляр этого класса
            // и держи его, я буду просить через @Autowired
public class JwtUtil {
    // Значения берём из application.properties
    // jwt.secret=lms_super_secret_key_2024_very_long_string_SSSSSS

    @Value("${jwt.secret}")
    private String secret;

    // jwt.expiration = 86400000 (24 hours)
    @Value("${jwt.expiration}")
    private Long expiration;

    // Создаём ключ подписи из нашей секретной строки
    // Этим ключом подписываем токен — без него токен нельзя подделать
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Token generation
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail()) // В токен кладем эмейл и тд
                .claim("role", RoleResolve.resolve(user))
                .claim("userId", user.getId())
                .setIssuedAt(new Date()) // время создания
                .setExpiration(  // время истечения
                        new Date(System.currentTimeMillis() + expiration)
                )
                .signWith(getSigningKey()) // подписываем секретным ключом
                .compact(); // собираем в строку "sdaGsdle...."
    }

    public String extractEmail(String token) {
        return parseClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return parseClaims(token).get("role", String.class);
    }

    public Long extractUserId(String token){
        return parseClaims(token).get("userId", Long.class);
    }

    public boolean isTokenValid(String token){
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims parseClaims(String token) { // расшифровка токена
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
