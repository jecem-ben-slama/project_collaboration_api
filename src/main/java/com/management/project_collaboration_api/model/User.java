package com.management.project_collaboration_api.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;
    private String resetToken;
    private LocalDateTime tokenExpiration;
    @Enumerated(EnumType.STRING)
    private Role role; // ADMIN or EMPLOYEE

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public enum Role {
        ADMIN, EMPLOYEE
    }
}