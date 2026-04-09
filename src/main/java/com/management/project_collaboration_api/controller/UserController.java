package com.management.project_collaboration_api.controller;

import com.management.project_collaboration_api.dto.PasswordChangeRequest;
import com.management.project_collaboration_api.dto.UserDTO;
import com.management.project_collaboration_api.model.User;
import com.management.project_collaboration_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
@PostMapping("/register")
public ResponseEntity<UserDTO> register(@RequestBody Map<String, Object> payload) {
    User user = new User();
    user.setName((String) payload.get("name"));
    user.setEmail((String) payload.get("email"));
    user.setPassword((String) payload.get("password"));
    user.setRole(User.Role.valueOf((String) payload.get("role")));

    // Get categoryId from payload
    Long categoryId = Long.valueOf(payload.get("categoryId").toString());

    return ResponseEntity.ok(userService.register(user, categoryId));
}

    @PostMapping("/change-password")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public String updatePassword(@RequestBody PasswordChangeRequest request) {
        userService.changePassword(request);
        return "Password updated successfully!";
    }

    // --- NEW: Forgot Password Endpoint ---
    // User provides email to receive a reset token
    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        userService.processForgotPassword(email);
        return "If an account exists with this email, a reset token has been sent.";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");

        // Call your service with the data from the body
        userService.resetPassword(token, newPassword);

        return "Password has been successfully reset.";
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDTO> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public UserDTO getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN', 'EMPLOYEE')") // Both Admin and Employee can update, but logic in service will handle restrictions
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id,
            @RequestBody Map<String, Object> payload) {

        User userDetails = new User();
        userDetails.setName((String) payload.get("name"));
        userDetails.setEmail((String) payload.get("email"));
        userDetails.setPassword((String) payload.get("password"));

        // Extract categoryId (might be null for Admins)
        Object catIdObj = payload.get("categoryId");
        Long categoryId = (catIdObj != null) ? Long.valueOf(catIdObj.toString()) : null;

        return ResponseEntity.ok(userService.updateUser(id, userDetails, categoryId));
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
}