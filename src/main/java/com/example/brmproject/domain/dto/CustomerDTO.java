package com.example.brmproject.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {
    private int id;

    private String name;

    private String phone;

    private String address;

    private String email;

    private Double debit;

    private Integer userId;
    @NotNull
    @Range(min=0,max=1000,message = "Range from 0-1000")
    private Double addDebit;
}
