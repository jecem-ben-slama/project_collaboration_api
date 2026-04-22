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


  @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
// Initialize to an empty ArrayList to prevent null issues
private List<Affectation> affectations = new ArrayList<>();
// If you are NOT using Lombok @Getter, add this manually:
public List<Affectation> getAffectations() {
    return affectations;}
    
    public enum ProjectStatus {
        PLANNED, IN_PROGRESS, COMPLETED, ON_HOLD
    }
}