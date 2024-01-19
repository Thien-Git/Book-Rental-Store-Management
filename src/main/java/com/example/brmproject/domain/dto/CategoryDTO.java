package com.example.brmproject.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    private int id;

    @NotBlank(message = "Category name is required")
    @Length(max = 100, message = "Max title is 100 letters")
    private String name;

    @NotBlank(message = "Category description is required")
    @Length(max = 225, message = "Max title is 225 letters")
    private String description;
//    private Collection<CategoryBookDTO> categoryBooksById;

}
