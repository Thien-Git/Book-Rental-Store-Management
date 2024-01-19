package com.example.brmproject.domain.dto;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import org.hibernate.validator.constraints.Range;


public class OrderFormDTO {

//    @Min(value = 1, message = "You have to add number min 1")
//    @Max(value = 10, message = "Max is 10")
    @Range(min=1, max=10, message = "Number of rend days must between 1 to 10")
    @NotNull
    private Integer rentDays;

    public OrderFormDTO(Integer rentDays) {
        this.rentDays = rentDays;
    }

    public OrderFormDTO() {
    }

    public Integer getRentDays() {
        return rentDays;
    }

    public void setRentDays(Integer rentDays) {
        this.rentDays = rentDays;
    }
}
