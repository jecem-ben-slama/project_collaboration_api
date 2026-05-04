package com.management.project_collaboration_api.service;

import com.management.project_collaboration_api.dto.PasswordChangeRequest;
import com.management.project_collaboration_api.dto.UserDTO;
import com.management.project_collaboration_api.model.Category;
import com.management.project_collaboration_api.model.User;
import com.management.project_collaboration_api.model.User.Role;
import com.management.project_collaboration_api.repository.CategoryRepository;
import com.management.project_collaboration_api.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private CategoryRepository categoryRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Inject the Email Service
    @Autowired
    private EmailService emailService;

    public List<UserDTO> getAll() {
        return userRepo.findAll().stream()
                .map(u -> modelMapper.map(u, UserDTO.class)).toList();
    }

    public List<UserDTO> getEmployees() {
        // We only fetch users with the 'EMPLOYEE' role
        return userRepo.findByRole(Role.EMPLOYEE).stream()
                .map(u -> modelMapper.map(u, UserDTO.class))
                .toList();
    }
    public UserDTO getById(Long id) {
        User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return modelMapper.map(user, UserDTO.class);
    }

    public UserDTO register(User user, Long categoryId) {
        // 1. Encode the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 2. Fetch the category from DB and link it
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        user.setCategory(category);

        // 3. Save and map to DTO
        User savedUser = userRepo.save(user);
        return modelMapper.map(savedUser, UserDTO.class);
    }

    public UserDTO updateUser(Long id, User userDetails, Long categoryId) {
        // 1. Find existing user
        User existingUser = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // 2. Update basic fields
        existingUser.setName(userDetails.getName());
        existingUser.setEmail(userDetails.getEmail());

        // CRITICAL: Update the Role
        // This allows switching between ADMIN and EMPLOYEE
        if (userDetails.getRole() != null) {
            existingUser.setRole(userDetails.getRole());
        }

        // Only update password if a new one is provided (usually for password reset)
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        // 3. Handle Category Update
        if (categoryId != null) {
            Category category = categoryRepo.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));
            existingUser.setCategory(category);
        } else {
            // If no categoryId is provided, check if it's an Admin
            // This ensures that if you switch an Admin to an Employee without a category,
            // the app behaves predictably.
            if (existingUser.getRole() == User.Role.ADMIN) {
                // Note: In your current system, we are using "IT Admin" category for Admins,
                // but this keeps the field flexible if you ever want to allow true nulls.
                existingUser.setCategory(null);
            }
        }

        // 4. Save and Map
        User updatedUser = userRepo.save(existingUser);

        // Log the successful update for debugging
        System.out.println("✅ User updated successfully: ID " + id +
                " | Role: " + updatedUser.getRole() +
                " | Category: " + (updatedUser.getCategory() != null ? updatedUser.getCategory().getLabel() : "None"));

        return modelMapper.map(updatedUser, UserDTO.class);
    }

    public void changePassword(PasswordChangeRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("The old password you entered is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepo.save(user);
    }

    // --- NEW: Forgot Password Logic ---
    public void processForgotPassword(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email"));

        // Generate unique token
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setTokenExpiration(LocalDateTime.now().plusHours(1));
        userRepo.save(user);

        // Send Email
        // Inside processForgotPassword
        String htmlBody = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: auto; border: 1px solid #ddd; padding: 20px;'>"
                +
                "<h2 style='color: #2c3e50;'>Password Reset Request</h2>" +
                "<p>Hello,</p>" +
                "<p>You requested to reset your password for the <strong>Project Collaboration Platform</strong>.</p>" +
                "<div style='text-align: center; margin: 30px 0;'>" +
                "<span style='background-color: #f1f1f1; padding: 10px 20px; font-size: 20px; font-weight: bold; border-radius: 5px; color: #333; letter-spacing: 2px;'>"
                + token + "</span>" +
                "</div>" +
                "<p>Please copy this token into the application to reset your password. This token will expire in 1 hour.</p>"
                +
                "<hr style='border: none; border-top: 1px solid #eee;'>" +
                "<p style='font-size: 12px; color: #777;'>If you did not request this, please ignore this email.</p>" +
                "</div>";

        emailService.sendHtmlEmail(user.getEmail(), "Reset Your Password", htmlBody);
    }

    // --- NEW: Reset Password Logic ---
    public void resetPassword(String token, String newPassword) {
        User user = userRepo.findByResetToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or non-existent reset token"));

        if (user.getTokenExpiration().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Reset token has expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));

        // Clear token so it's one-time use
        user.setResetToken(null);
        user.setTokenExpiration(null);
        userRepo.save(user);
    }

    public void delete(Long id) {
        userRepo.deleteById(id);
    }
}