package com.example.brmproject.service.imp;

import com.example.brmproject.domain.dto.CategoryBookDTO;
import com.example.brmproject.domain.entities.CategoryBookEntity;
import com.example.brmproject.repositories.CategoryBookEntityRepository;
import com.example.brmproject.service.CategoryBookService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryBookServiceImp implements CategoryBookService {

    private ModelMapper modelMapper;
    private CategoryBookEntityRepository categoryBookEntityRepository;
    @Autowired
    public CategoryBookServiceImp(ModelMapper modelMapper, CategoryBookEntityRepository categoryBookEntityRepository) {
        this.modelMapper = modelMapper;
        this.categoryBookEntityRepository = categoryBookEntityRepository;
    }

    @Override
    public CategoryBookDTO add(CategoryBookDTO categoryBookDTO) {
        CategoryBookEntity categoryBookEntity = categoryBookEntityRepository.save(mapToEntity(categoryBookDTO));
        return mapToDTO(categoryBookEntity);
    }

    @Override
    public Page<CategoryBookDTO> findBooksByCategoryId(int id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CategoryBookEntity> categoryBookEntities = categoryBookEntityRepository.findByCategoryId(id, pageable);
        return categoryBookEntities.map(this::mapToDTO);
    }

    @Override
    public List<CategoryBookDTO> findByCategoryId(Integer categoryId) {
        List<CategoryBookEntity> categoryBookEntity = categoryBookEntityRepository.findByCategoryId(categoryId);
        return categoryBookEntity
                .stream()
                .map(categoryBook -> mapToDTO(categoryBook))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void updateCategoryBook(Integer categoryBookId, Integer categoryId) {
        categoryBookEntityRepository.updateCategoryBook(categoryBookId, categoryId);
    }

    @Override
    public List<CategoryBookDTO> findByBookId(Integer bookId) {
        List<CategoryBookEntity> categoryBookEntity = categoryBookEntityRepository.findByBookId(bookId);
        return categoryBookEntity
                .stream()
                .map(categoryBook -> mapToDTO(categoryBook))
                .collect(Collectors.toList());
    }

    public CategoryBookDTO mapToDTO(CategoryBookEntity categoryBookEntity) {
        return modelMapper.map(categoryBookEntity, CategoryBookDTO.class);
    }

    public CategoryBookEntity mapToEntity(CategoryBookDTO categoryBookDTO) {
        return modelMapper.map(categoryBookDTO, CategoryBookEntity.class);
    }
}
