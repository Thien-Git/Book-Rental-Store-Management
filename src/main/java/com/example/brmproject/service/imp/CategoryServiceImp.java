package com.example.brmproject.service.imp;

import com.example.brmproject.domain.dto.CategoryDTO;
import com.example.brmproject.domain.entities.CategoryEntity;
import com.example.brmproject.repositories.CategoryEntityRepository;
import com.example.brmproject.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImp implements CategoryService {


    private CategoryEntityRepository categoryEntityRepository;

    private ModelMapper modelMapper;
    @Autowired
    public CategoryServiceImp(CategoryEntityRepository categoryEntityRepository, ModelMapper modelMapper) {
        this.categoryEntityRepository = categoryEntityRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryDTO addNewCategory(CategoryDTO categoryDTO) {
        CategoryEntity categoryEntity = categoryEntityRepository.save(mapToEntity(categoryDTO));
        return mapToDTO(categoryEntity);
    }

    @Override
    public List<CategoryDTO> findAll() {
        return categoryEntityRepository.findAll().stream()
                .map(categoryEntity -> mapToDTO(categoryEntity)).collect(Collectors.toList());
    }

    @Override
    public CategoryDTO findById(Integer id) {
        CategoryEntity categoryEntity = categoryEntityRepository.findById(id).orElse(null);
        return mapToDTO(categoryEntity);
    }

    @Transactional
    @Override
    public void updateCategory(CategoryDTO categoryDTO) {
        CategoryEntity existedCategory = categoryEntityRepository.findById(categoryDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + categoryDTO.getId()));

        existedCategory.setName(categoryDTO.getName());
        existedCategory.setDescription(categoryDTO.getDescription());
        CategoryEntity updateCategory = categoryEntityRepository.save(existedCategory);
        mapToDTO(updateCategory);

    }

    public CategoryDTO mapToDTO(CategoryEntity categoryEntity) {
        return modelMapper.map(categoryEntity, CategoryDTO.class);
    }

    public CategoryEntity mapToEntity(CategoryDTO categoryDTO) {
        return modelMapper.map(categoryDTO, CategoryEntity.class);

    }
}
