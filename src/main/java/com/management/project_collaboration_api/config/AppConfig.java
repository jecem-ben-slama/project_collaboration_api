package com.management.project_collaboration_api.config;

import com.management.project_collaboration_api.dto.UserDTO;
import com.management.project_collaboration_api.model.User;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        // Custom converter for User to UserDTO
        Converter<User, UserDTO> userToDtoConverter = context -> {
            User user = context.getSource();
            UserDTO dto = new UserDTO();
            dto.setId(user.getId());
            dto.setName(user.getName());
            dto.setEmail(user.getEmail());
            dto.setRole(user.getRole().toString());
            if (user.getCategory() != null) {
                dto.setCategoryId(user.getCategory().getId());
                dto.setCategoryLabel(user.getCategory().getLabel());
            }
            return dto;
        };
        modelMapper.createTypeMap(User.class, UserDTO.class).setConverter(userToDtoConverter);
        return modelMapper;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}