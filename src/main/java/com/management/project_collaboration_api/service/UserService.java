package com.management.project_collaboration_api.service;

import com.management.project_collaboration_api.dto.UserDTO;
import com.management.project_collaboration_api.model.User;
import com.management.project_collaboration_api.repository.UserRepository;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
private PasswordEncoder passwordEncoder;

    public List<UserDTO> getAll() {
        return userRepo.findAll().stream()
                .map(u -> modelMapper.map(u, UserDTO.class)).toList();
    }

    public UserDTO getById(Long id) {
        User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return modelMapper.map(user, UserDTO.class);
    }

    public UserDTO register(User user) {
        // Encode the password before saving
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        return modelMapper.map(userRepo.save(user), UserDTO.class);
    }

    public UserDTO update(Long id, User details) {
        User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setName(details.getName());
        user.setEmail(details.getEmail());
        user.setRole(details.getRole());
        user.setCategory(details.getCategory());
        return modelMapper.map(userRepo.save(user), UserDTO.class);
    }

    public void delete(Long id) {
        userRepo.deleteById(id);
    }
}