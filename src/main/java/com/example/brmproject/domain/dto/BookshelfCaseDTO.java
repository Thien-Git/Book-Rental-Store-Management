package com.example.brmproject.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookshelfCaseDTO {
    private int id;

    @Pattern(regexp = "^BS-[A-Z]-\\d{2}$", message = "Invalid format. Should be BS-A-01 (A is a letter and 01 is a number)")
    @NotBlank
    private String bookshelfCode;

    private Integer caseNumber;
    private Integer capacity;

    private Integer availableBlank;
    private Collection<BookDTO> booksById;

    public Integer getAvailableBlank() {
        return countAvailableBlank();
    }

    public Integer countAvailableBlank()
    {
        if(booksById!=null){
        Long count= 0L;
        for (BookDTO book:booksById)
        {
            if(book!=null)
            {
             count+=book.getExistBook();
            }
        }
            return capacity-count.intValue();
        }
        return capacity;
    }


}
