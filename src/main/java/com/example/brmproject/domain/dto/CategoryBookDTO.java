package com.example.brmproject.domain.dto;

import com.example.brmproject.domain.entities.BookEntity;
import com.example.brmproject.domain.entities.CategoryEntity;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryBookDTO {
    private int id;
    private Integer categoryId;
    private Integer bookId;

    public CategoryBookDTO(Integer categoryId, Integer bookId) {
        this.categoryId = categoryId;
        this.bookId = bookId;
    }
    private CategoryDTO categoryByBookId;
    private BookDTO bookByBookId;
}
