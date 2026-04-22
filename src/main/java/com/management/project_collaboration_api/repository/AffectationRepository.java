package com.management.project_collaboration_api.repository;

import com.management.project_collaboration_api.model.Affectation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AffectationRepository extends JpaRepository<Affectation, Long> {
    List<Affectation> findByProjectId(Long projectId);

    Optional<Affectation> findByProjectIdAndUserId(Long projectId, Long userId);
    List<Affectation> findByUserId(Long userId);
    
    boolean existsByUserIdAndProjectId(Long userId, Long projectId);
    
}