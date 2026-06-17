package com.example.lms.backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "students")
@DiscriminatorValue("STUDENT")
@Data
@EqualsAndHashCode(callSuper=true)
@SuperBuilder

public class Student extends User {
    private String groupNumber;
}
