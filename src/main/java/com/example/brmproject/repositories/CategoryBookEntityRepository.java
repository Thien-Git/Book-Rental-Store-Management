package com.example.brmproject.repositories;

import com.example.brmproject.domain.dto.CategoryBookDTO;
import com.example.brmproject.domain.dto.CategoryDTO;
import com.example.brmproject.domain.entities.CategoryBookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryBookEntityRepository extends JpaRepository<CategoryBookEntity, Integer> {
    Page<CategoryBookEntity> findByCategoryId(Integer id, Pageable pageable);

    List<CategoryBookEntity> findByCategoryId(Integer categoryId);

    List<CategoryBookEntity> findByBookId(Integer bookId);

    @Modifying
    @Query("UPDATE CategoryBookEntity cb SET cb.categoryId = :categoryId WHERE cb.id = :categoryBookId")
    void updateCategoryBook(@Param("categoryBookId") Integer categoryBookId,
                                  @Param("categoryId") Integer categoryId);

}