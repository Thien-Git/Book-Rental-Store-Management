package com.example.brmproject.service.imp;

import com.example.brmproject.domain.dto.BookDTO;
import com.example.brmproject.domain.dto.BookshelfCaseDTO;
import com.example.brmproject.domain.entities.BookEntity;
import com.example.brmproject.domain.entities.BookshelfCaseEntity;
import com.example.brmproject.exception.ResourceNotFoundException;
import com.example.brmproject.repositories.BookEntityRepository;
import com.example.brmproject.repositories.BookshelfCaseEntityRepository;
import com.example.brmproject.service.BookShelfService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookShelfServiceImp implements BookShelfService {

    BookshelfCaseEntityRepository bookShelfrepository;
    BookEntityRepository bookRepository;
    ModelMapper modelMapper;

    @Autowired
    public BookShelfServiceImp(BookshelfCaseEntityRepository bookShelfrepository, BookEntityRepository bookRepository, ModelMapper modelMapper) {
        this.bookShelfrepository = bookShelfrepository;
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean create(BookshelfCaseDTO bookCaseDTO) {
        String shelfCode =  bookCaseDTO.getBookshelfCode();
        if (shelfCode != null) {
            List<BookshelfCaseEntity> checkShelf = bookShelfrepository.validBookCaseByCode(shelfCode);
            if(checkShelf.size() == 0){
                for (int i = 1; i <= 5; i++)
                {
                    BookshelfCaseEntity newShelf=new BookshelfCaseEntity();
                 newShelf.setBookshelfCode(bookCaseDTO.getBookshelfCode());
                 newShelf.setCaseNumber(i);
                 newShelf.setCapacity(20);
                 bookShelfrepository.save(newShelf);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public List<BookshelfCaseDTO> showAll() {
        List<BookshelfCaseDTO> list= bookShelfrepository.findAll().stream().map(bs->mapToDTO(bs)).collect(Collectors.toList());
        return list;
    }

    @Override
    public BookshelfCaseDTO findById(Integer id) {

        BookshelfCaseEntity myCase= bookShelfrepository.findById(id).orElseThrow(()->new ResourceNotFoundException("BookShelf","id",String.valueOf(id)));

        return mapToDTO(myCase);
    }

    @Override
    public BookshelfCaseDTO update(BookshelfCaseDTO bookshelfCaseDTO) {
        return null;
    }

    @Override
    public List<BookshelfCaseDTO> findBlankCase(long numberOfBook) {
        List<BookshelfCaseDTO> listAll = bookShelfrepository.findAll().stream().map(bc->mapToDTO(bc)).collect(Collectors.toList());
        List<BookshelfCaseDTO> list = listAll.stream().filter(bc->bc.getAvailableBlank()>numberOfBook).limit(10).collect(Collectors.toList());
        return list;
    }

    @Override
    public BookshelfCaseDTO searchByBookId(Integer bookId) {
        BookEntity myBook=bookRepository.findById(bookId).orElseThrow(()->new ResourceNotFoundException("Book","id",String.valueOf(bookId)));
        BookDTO bookDTO=mapBookToDTO(myBook);

        return bookDTO.getBookshelfCaseByBookshelfId();
    }


    public BookshelfCaseDTO mapToDTO(BookshelfCaseEntity bookCase) {
        BookshelfCaseDTO caseDTO = modelMapper.map(bookCase, BookshelfCaseDTO.class);
        return caseDTO;

    }

    public BookshelfCaseEntity mapToEntity(BookshelfCaseDTO caseDTO) {
        BookshelfCaseEntity bookCase = modelMapper.map(caseDTO, BookshelfCaseEntity.class);
        return bookCase;
    }
    public BookDTO mapBookToDTO(BookEntity book) {
        BookDTO bookDTO = modelMapper.map(book, BookDTO.class);
        return bookDTO;

    }

    public BookEntity mapBookToEntity(BookDTO bookDTO) {
        BookEntity book = modelMapper.map(bookDTO, BookEntity.class);
        return book;
    }
}
