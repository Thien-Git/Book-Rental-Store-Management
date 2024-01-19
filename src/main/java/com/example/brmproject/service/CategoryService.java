package com.example.brmproject.service;

import com.example.brmproject.domain.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {

    CategoryDTO addNewCategory(CategoryDTO categoryDTO);

    List<CategoryDTO> findAll();

    CategoryDTO findById(Integer id);

    void updateCategory(CategoryDTO categoryDTO);
}
