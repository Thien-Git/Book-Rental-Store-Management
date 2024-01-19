package com.example.brmproject.service;

import com.example.brmproject.domain.dto.BookDTO;
import com.example.brmproject.domain.dto.BookDetailDTO;

import java.util.List;

public interface BookDetailService {

    void updateStatus(BookDetailDTO bookDetailDTO, String status);

    void updateStatusByBookId(Integer bookId, String status);

    void updateStatusByid(Integer bookDetailId, String status);

    BookDTO countAvailable(Integer bookId);

    void addBookDetails(Integer bookId, Integer numberBD);

    BookDetailDTO addBookDetails(BookDetailDTO bookDetailDTO);

    List<BookDetailDTO> getAllBooks(Integer bookId);

    List<BookDetailDTO> findAllBookDetail(Integer bookId);

}
