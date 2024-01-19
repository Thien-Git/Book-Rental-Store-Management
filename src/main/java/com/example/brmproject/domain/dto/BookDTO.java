package com.example.brmproject.domain.dto;

import com.example.brmproject.ultilities.SD.BookDetailStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
    private int id;

    @NotBlank(message = "Title is required")
    @Length(max = 100, message = "Max title is 100 letters")
    private String title;

    @NotBlank(message = "Author is required")
    @Length(max = 100, message = "Max title is 100 letters")
    private String author;

    @NotBlank(message = "Title is required")
    private String preview;

    @NotNull(message = "Price per day is required")
    @Range(min = 0, max = 2, message = "Price must be from 0 to 2")
    private Double pricePerDay;

    @NotBlank(message = "Photo is required")
    private String photo;

    private Integer bookshelfId;

    @NotNull
    private List<Integer> categoriesId;

    private Integer quantityBooks;

    private double rating;

    private Collection<BookDetailDTO> bookDetailsById;

    // show availableBook.
    private Long availableBook;
    private Long existBook;

    private Integer numberOfImport;



    private BookshelfCaseDTO bookshelfCaseByBookshelfId;

    // private Collection<OrderDetailDTO> orderDetailsById;



    public Long getAvailableBook() {
        return countAvailable();
    }

    public Long getExistBook() {
        return countExits();
    }

    // ... (rest of the class)

    public Long countAvailable() {
        return bookDetailsById.stream()
                .filter(bd -> BookDetailStatus.AVAILABLE.toString().equals(bd.getStatus()))
                .count();
    }

    public Long countExits() {
        return bookDetailsById.stream()
                .filter(bd -> !BookDetailStatus.LOST.toString().equals(bd.getStatus()))
                .count();
    }

}
