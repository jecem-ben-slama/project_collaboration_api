package com.management.project_collaboration_api.repository;

import com.management.project_collaboration_api.model.Project;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DashboardRepository extends JpaRepository<Project, Long> {

    @Query("SELECT p.status, COUNT(p) FROM Project p GROUP BY p.status")
    List<Object[]> countProjectsByStatus();

    @Query("SELECT a.user.name, COUNT(a) FROM Affectation a GROUP BY a.user.id, a.user.name ORDER BY COUNT(a) DESC")
    List<Object[]> findUserWorkload(Pageable pageable);

    // 1. Projects monthly trend
    @Query(value = "SELECT DATE_FORMAT(created_at, '%Y-%m') as month, COUNT(*) as count " +
            "FROM project GROUP BY month ORDER BY month DESC LIMIT 6", nativeQuery = true)
    List<Object[]> findProjectTrends();

    // 2. Notes daily trend
    @Query(value = "SELECT DATE_FORMAT(created_at, '%Y-%m-%d') as day, COUNT(*) as count " +
            "FROM note GROUP BY day ORDER BY day DESC LIMIT 7", nativeQuery = true)
    List<Object[]> findNoteTrendsPerDay();

    // 3. Notes per user - JOINING on user_id as seen in your screenshot
    @Query(value = "SELECT u.name, COUNT(n.id) FROM users u " +
            "LEFT JOIN note n ON u.id = n.user_id " +
            "GROUP BY u.id, u.name", nativeQuery = true)
    List<Object[]> findNotesCountPerUser();
}