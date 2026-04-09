package com.management.project_collaboration_api.config;

import com.management.project_collaboration_api.model.User;
import com.management.project_collaboration_api.repository.CategoryRepository;
import com.management.project_collaboration_api.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepo,
            CategoryRepository categoryRepo,
            PasswordEncoder passwordEncoder) {
        return args -> {
            // 1. Create Default Categories if they don't exist

            // 2. Create Admin User if doesn't exist
            if (userRepo.findByEmail("admin@system.com").isEmpty()) {

                User admin = new User();
                admin.setName("System Admin");
                admin.setEmail("admin@system.com");
                admin.setPassword(passwordEncoder.encode("admin123")); // Change this!
                admin.setRole(User.Role.ADMIN);

                userRepo.save(admin);
                System.out.println("✅ Admin user seeded: admin@system.com / admin123");
            }
        };
    }
}