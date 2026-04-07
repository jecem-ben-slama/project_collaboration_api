package com.management.project_collaboration_api.repository;

import com.management.project_collaboration_api.model.Affectation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AffectationRepository extends JpaRepository<Affectation, Long> {
    List<Affectation> findByProjectId(Long projectId);

    List<Affectation> findByUserId(Long userId);
}