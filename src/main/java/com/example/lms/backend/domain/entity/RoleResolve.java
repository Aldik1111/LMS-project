package com.example.lms.backend.domain.entity;

public class RoleResolve {
    public static String resolve(User user) {
        if (user instanceof Admin) return "ADMIN";
        if (user instanceof Manager) return "MANAGER";
        if (user instanceof Student) return "STUDENT";
        throw new IllegalStateException("Неизвестный тип юзера: " + user.getClass());
    }
}