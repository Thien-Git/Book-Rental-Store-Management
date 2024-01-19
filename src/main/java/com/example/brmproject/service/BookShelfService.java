package com.example.brmproject.service;

import com.example.brmproject.domain.dto.BookshelfCaseDTO;

import java.util.List;

public interface BookShelfService {

    boolean create(BookshelfCaseDTO bookCaseDTO);
    List<BookshelfCaseDTO> showAll();
    BookshelfCaseDTO findById(Integer id);

    BookshelfCaseDTO update(BookshelfCaseDTO bookshelfCaseDTO);

    List<BookshelfCaseDTO> findBlankCase(long numberOfBook);

    BookshelfCaseDTO searchByBookId(Integer bookId);

}
