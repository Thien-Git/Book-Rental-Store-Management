package com.example.brmproject.controller.staff;

import com.example.brmproject.domain.dto.*;
import com.example.brmproject.service.*;
import com.example.brmproject.ultilities.SD.BookDetailStatus;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/staff")
public class BookStaffController {


    private BookService bookService;
    private CategoryService categoryService;
    private CategoryBookService categoryBookService;
    private BookDetailService bookDetailService;

    private BookShelfService bookShelfService;
    @Autowired
    public BookStaffController(BookService bookService, CategoryService categoryService, CategoryBookService categoryBookService, BookDetailService bookDetailService, BookShelfService bookShelfService) {
        this.bookService = bookService;
        this.categoryService = categoryService;
        this.categoryBookService = categoryBookService;
        this.bookDetailService = bookDetailService;
        this.bookShelfService = bookShelfService;
    }

    @GetMapping("/books/new")
    public String getCreateBookPage(Model model) {
        try {
            List<CategoryDTO> categories = categoryService.findAll();
            model.addAttribute("book", new BookDTO());
            model.addAttribute("categories", categories);
            return "adminTemplate/books/addNewBook";
        } catch ( Exception e) {
            model.addAttribute("message",e.getMessage());
            return "/error";
        }
    }

    @PostMapping("/books/new")
    public String createNewBook(@ModelAttribute("book") @Valid BookDTO bookDTO,
                                BindingResult bindingResult,
                                Model model,
                                @RequestParam(name = "categoriesId", required = false) List<Integer> categoriesId) {

        if(bindingResult.hasErrors()) {
            List<CategoryDTO> categories = categoryService.findAll();
            model.addAttribute("categories", categories);
            model.addAttribute("book", bookDTO);
            return "adminTemplate/books/addNewBook";
        }
        BookDTO newBook = bookService.addNewBook(bookDTO);

        for (Integer categoryId : categoriesId) {
            CategoryBookDTO categoryBookDTO = new CategoryBookDTO();
            categoryBookDTO.setCategoryByBookId(categoryService.findById(categoryId));
            categoryBookDTO.setBookByBookId(newBook);
            categoryBookService.add(categoryBookDTO);
        }
        return "redirect:/staff/books";
    }

    @GetMapping("/books/import-quantity/{bookId}")
    public String getImportBookQuantityForm(@PathVariable Integer bookId,
                                             Model model) {
        BookDTO book = bookService.findBookById(bookId);
        model.addAttribute("book", book);
        return "adminTemplate/books/importBookQuantity";
    }

    @PostMapping("/books/import-quantity/{bookId}")
    public String importBooks(@PathVariable Integer bookId,
                              @ModelAttribute("book") BookDTO bookDTO,
                              Model model) {
        BookDTO book = bookService.findBookById(bookId);
        if (bookDTO.getQuantityBooks() != null) {
            for (int i = 0; i < bookDTO.getQuantityBooks(); i++) {
                BookDetailDTO bookDetailDTO = new BookDetailDTO();
                bookDetailDTO.setBookByBookId(book);
                bookDetailDTO.setStatus(String.valueOf(BookDetailStatus.AVAILABLE));
                DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                bookDetailDTO.setImportDate(LocalDateTime.now().format(formatter));
                bookDetailService.addBookDetails(bookDetailDTO);
            }
        }
        return "redirect:/staff/books";
    }

    @GetMapping("/books/showAll")
    public String showAll(Model model, @ModelAttribute BookDTO book) {
        try {
            List<BookDTO> list = bookService.findAvailableBook();
            model.addAttribute("books",list);
            return "customerTemplate/showAllBook";
        } catch ( Exception e) {
            model.addAttribute("message",e.getMessage());
            return "/error";
        }
    }

    @GetMapping("/books")
    public String getAllBooksAdmin(Model model,
                                   @ModelAttribute BookDTO book,
                                   @RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "6") int size) {
        try {
            Page<BookDTO> listBooks = bookService.findAllBooks(page - 1, size);
            int totalPages = listBooks.getTotalPages();
            if (totalPages > 0) {
                List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                        .boxed()
                        .collect(Collectors.toList());
                model.addAttribute("pageNumbers", pageNumbers);
            }
            model.addAttribute("books",listBooks);
            return "adminTemplate/books/showAllBook";
        } catch ( Exception e) {
            model.addAttribute("message",e.getMessage());
            return "/error";
        }
    }

    @GetMapping("/books/update/{bookId}")
    public String getUpdateBookPage(@PathVariable Integer bookId,
                                    Model model) {
        try {
            BookDTO findBook = bookService.findBookById(bookId);

            List<String> categoriesOfBook= categoryBookService.findByBookId(bookId)
                    .stream().map(categoryBook -> categoryBook.getCategoryByBookId().getName())
                    .collect(Collectors.toList());

            List<CategoryDTO> categoryList = categoryService.findAll();
            model.addAttribute("categoriesOfBook", categoriesOfBook);
            model.addAttribute("categories", categoryList);
            model.addAttribute("book", findBook);
            return "adminTemplate/books/updateBook";
        } catch ( Exception e) {
            model.addAttribute("message",e.getMessage());
            return "/error";
        }
    }

    @PostMapping("/books/update/{bookId}")
    public String updateBook(Model model,
                             @ModelAttribute("newBook") BookDTO bookDTO,
                             @PathVariable Integer bookId) {
        try {
            BookDTO findBook = bookService.findBookById(bookId);
            if (!bookDTO.getTitle().trim().isEmpty()) {
                findBook.setTitle(bookDTO.getTitle());
            }
            if (!bookDTO.getAuthor().trim().isEmpty()) {
                findBook.setAuthor(bookDTO.getAuthor());
            }
            if (!bookDTO.getPreview().trim().isEmpty()) {
                findBook.setPreview(bookDTO.getPreview());
            }
            if (bookDTO.getPricePerDay() > 0) {
                findBook.setPricePerDay(bookDTO.getPricePerDay());
            }
            if (!bookDTO.getPhoto().trim().isEmpty()) {
                findBook.setPhoto(bookDTO.getPhoto());
            }
            bookService.addNewBook(findBook);
            return "redirect:/staff/books";
        } catch ( Exception e) {
            model.addAttribute("message",e.getMessage());
            return "/error";
        }
    }

//change bookshelf
    @GetMapping("/books/detail/{bookId}")
    @PreAuthorize("hasAuthority('STAFF') or hasAuthority('ADMIN')")
    public String showDetail(Model model,@PathVariable Integer bookId) {
            BookDTO bookDTO=bookService.findBookById(bookId);
            List<BookshelfCaseDTO> list=bookShelfService.findBlankCase(bookDTO.getExistBook());
            model.addAttribute("bookDTO",bookDTO);
            model.addAttribute("list",list);

        return "adminTemplate/books/change-bookshelf";
    }

    @PostMapping("/books/change-case")
    @PreAuthorize("hasAuthority('STAFF') or hasAuthority('ADMIN')")
    public String changeCase(Model model,@ModelAttribute BookDTO bookDTO)
    {
        bookService.changeBookCase(bookDTO.getId(),bookDTO.getBookshelfId());

        return "redirect:/staff/books";
    }

    @GetMapping("/books/showBookDetail/{bookId}")
    public String showBookDetail(@PathVariable Integer bookId, Model model) {
        List<BookDetailDTO> bookDetails = bookDetailService.findAllBookDetail(bookId);
        model.addAttribute("bookDetails", bookDetails);
        System.out.println(bookDetails);
        return "adminTemplate/books/bookDetail";
    }




}

