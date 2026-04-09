package com.management.project_collaboration_api.service;

import com.management.project_collaboration_api.dto.CategoryDTO;
import com.management.project_collaboration_api.model.Category;
import com.management.project_collaboration_api.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepo;
    @Autowired
    private ModelMapper modelMapper;

    public List<CategoryDTO> getAll() {
        return categoryRepo.findAll().stream()
                .map(c -> modelMapper.map(c, CategoryDTO.class)).toList();
    }

    public CategoryDTO getById(Long id) {
        Category cat = categoryRepo.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        return modelMapper.map(cat, CategoryDTO.class);
    }

    public CategoryDTO save(CategoryDTO dto) {
        Category cat = modelMapper.map(dto, Category.class);
        return modelMapper.map(categoryRepo.save(cat), CategoryDTO.class);
    }

    public CategoryDTO update(Long id, CategoryDTO dto) {
        Category cat = categoryRepo.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        cat.setLabel(dto.getLabel());
        return modelMapper.map(categoryRepo.save(cat), CategoryDTO.class);
    }


public Category findById(Long id) {
    return categoryRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found"));
}

    public void delete(Long id) {
        categoryRepo.deleteById(id);
    }
}