package com.example.brmproject.domain.dto;

import com.example.brmproject.domain.entities.BookEntity;
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
public class BookDetailDTO {
    private int id;

    private Integer bookId;

    private String status;

    private String importDate;

    private BookDTO bookByBookId;

}
