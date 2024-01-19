package com.example.brmproject.service.imp;

import com.example.brmproject.domain.dto.BookDTO;
import com.example.brmproject.domain.dto.BookDetailDTO;
import com.example.brmproject.domain.entities.BookDetailEntity;
import com.example.brmproject.domain.entities.BookEntity;
import com.example.brmproject.exception.ResourceNotFoundException;
import com.example.brmproject.repositories.BookDetailEntityRepository;
import com.example.brmproject.repositories.BookEntityRepository;
import com.example.brmproject.service.BookDetailService;
import com.example.brmproject.ultilities.SD.BookDetailStatus;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookDetailServiceImp implements BookDetailService {


    private BookEntityRepository bookRepo;
    private BookDetailEntityRepository bookDetailRepo;

    private ModelMapper modelMapper;

    @Autowired
    public BookDetailServiceImp(BookEntityRepository bookRepo, BookDetailEntityRepository bookDetailRepo, ModelMapper modelMapper) {
        this.bookRepo = bookRepo;
        this.bookDetailRepo = bookDetailRepo;
        this.modelMapper = modelMapper;
    }


    @Override
    public void updateStatus(BookDetailDTO bookDetailDTO,String status) {

            bookDetailDTO.setStatus(status);
            bookDetailRepo.save(mapToEntity(bookDetailDTO));
    }
    @Override
    public void updateStatusByid(Integer bookDetailId,String status)
    {
        BookDetailEntity bookDetail=bookDetailRepo.findById(bookDetailId).get();
        bookDetail.setStatus(status);
        bookDetailRepo.save(bookDetail);
    }
    @Override
    public void updateStatusByBookId(Integer bookId, String status) {

       BookDetailEntity bookDetail= bookDetailRepo.findByBookId(bookId).stream().collect(Collectors.toList()).stream().findAny().get();
       bookDetail.setStatus(status);
       bookDetailRepo.save(bookDetail);
    }
    //count available to check
    @Override
    public BookDTO countAvailable(Integer bookId) {
        BookEntity book= bookRepo.findById(bookId).orElseThrow(()->new ResourceNotFoundException("Book","Id",String.valueOf(bookId)));
        Long availableBook= book.getBookDetailsById().stream().filter(b->b.getStatus().equalsIgnoreCase(String.valueOf(BookDetailStatus.AVAILABLE))).count();
       //add count to book dto
        BookDTO bookDTO= mapBookToDTO(book);
        bookDTO.setAvailableBook(availableBook);
        return bookDTO;
    }
    @Override
    public void addBookDetails(Integer bookId,Integer numberBD) {


    }

    @Override
    public BookDetailDTO addBookDetails(BookDetailDTO bookDetailDTO) {
        BookDetailEntity bookDetailEntity = bookDetailRepo.save(mapToEntity(bookDetailDTO));
        return mapToDTO(bookDetailEntity);
    }

    @Override
    public List<BookDetailDTO> getAllBooks(Integer bookId) {
        List<BookDetailEntity> booksEntity = bookDetailRepo.findByBookId(bookId);
        return booksEntity.stream()
                .map(book -> mapToDTO(book))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDetailDTO> findAllBookDetail(Integer bookId) {

        List<BookDetailEntity> list = bookDetailRepo.findByBookId(bookId);

        List<BookDetailDTO> bookDetailDTOList =  list.stream()
                .map(book -> mapToDTO(book)).collect(Collectors.toList());

//        List<BookDetailDTO> list1 = new ArrayList<>();
//
//        for (BookDetailEntity entity :list) {
//            BookDetailDTO bookDetailDTO = new BookDetailDTO();
//
//            bookDetailDTO.setId(entity.getId());
//            bookDetailDTO.setStatus(entity.getStatus());
//            bookDetailDTO.setBookId(entity.getBookId());
//            bookDetailDTO.setImportDate(entity.getImportDate());
//
//            list1.add(bookDetailDTO);
//        }
//        return list1;
        return bookDetailDTOList;
    }


    public BookDetailDTO mapToDTO(BookDetailEntity bookDetail) {
        BookDetailDTO bookDetailDTO = modelMapper.map(bookDetail, BookDetailDTO.class);
        return bookDetailDTO;

    }

    public BookDetailEntity mapToEntity(BookDetailDTO ordersDTO) {
        BookDetailEntity bookDetail = modelMapper.map(ordersDTO, BookDetailEntity.class);
        return bookDetail;

    }
    public BookDTO mapBookToDTO(BookEntity book) {
        BookDTO bookDTO = modelMapper.map(book, BookDTO.class);
        return bookDTO;

    }

    public BookEntity mapBookToEntity(BookDTO bookDTO) {
        BookEntity book = modelMapper.map(bookDTO, BookEntity.class);
        return book;

    }
//    public BookAvailableDTO mapAvailableBookToDTO(BookEntity book) {
//        BookAvailableDTO bookDTO = modelMapper.map(book, BookAvailableDTO.class);
//        return bookDTO;
//
//    }
//
//    public BookEntity mapAvailableBookToEntity(BookAvailableDTO bookDTO) {
//        BookEntity book = modelMapper.map(bookDTO, BookEntity.class);
//        return book;
//
//    }
}
