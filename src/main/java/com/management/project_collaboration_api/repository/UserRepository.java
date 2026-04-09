package com.management.project_collaboration_api.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.management.project_collaboration_api.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    // Used for Login and Security Context
    Optional<User> findByEmail(String email);

    // Used for the Reset Password flow
    Optional<User> findByResetToken(String resetToken);
}