package com.management.project_collaboration_api.config;

import com.management.project_collaboration_api.model.Category;
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
            // 1. Create Default Categories
            // We check by name to avoid duplicate entries on every restart
            Category itAdminCategory;
            if (categoryRepo.findByLabel("IT Admin").isEmpty()) {
                Category newCategory = new Category();
                newCategory.setLabel("IT Admin");
                // Note: ID is usually auto-generated. If the DB is empty, this will be 1.
                itAdminCategory = categoryRepo.save(newCategory);
                System.out.println("✅ Category seeded: IT Admin");
            } else {
                itAdminCategory = categoryRepo.findByLabel("IT Admin").get();
            }

            // 2. Create Admin User if doesn't exist
            if (userRepo.findByEmail("admin@system.com").isEmpty()) {
                User admin = new User();
                admin.setName("System Admin");
                admin.setEmail("admin@system.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(User.Role.ADMIN);

                // Link the Admin to the "IT Admin" category so the backend doesn't crash
                admin.setCategory(itAdminCategory);

                userRepo.save(admin);
                System.out.println("✅ Admin user seeded: admin@system.com / admin123");
            }
        };
    }
}