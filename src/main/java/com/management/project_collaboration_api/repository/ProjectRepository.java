package com.management.project_collaboration_api.repository;

import com.management.project_collaboration_api.model.Project;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
   @Query("SELECT DISTINCT p FROM Project p LEFT JOIN FETCH p.affectations a LEFT JOIN FETCH a.user")
    List<Project> findAllWithDetails();
}