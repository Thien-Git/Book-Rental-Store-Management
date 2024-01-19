package com.example.brmproject.service;

import com.example.brmproject.domain.dto.CategoryBookDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryBookService {

    CategoryBookDTO add(CategoryBookDTO categoryBookDTO);

    List<CategoryBookDTO> findByCategoryId(Integer categoryId);

    Page<CategoryBookDTO> findBooksByCategoryId(int id, int page, int size);

    void updateCategoryBook(Integer categoryBookId, Integer categoryId);

    List<CategoryBookDTO> findByBookId(Integer bookId);
}
