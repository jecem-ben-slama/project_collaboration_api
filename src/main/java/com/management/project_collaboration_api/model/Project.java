package com.management.project_collaboration_api.model;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description; 

    @Enumerated(EnumType.STRING)
    private ProjectStatus status = ProjectStatus.PLANNED;

    // ADD THIS FIELD
    @Column(name = "created_at", updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private java.time.LocalDateTime createdAt;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<Affectation> affectations = new ArrayList<>();

    public List<Affectation> getAffectations() {
        return affectations;
    }

    public enum ProjectStatus {
        PLANNED, IN_PROGRESS, COMPLETED, ON_HOLD
    }
}