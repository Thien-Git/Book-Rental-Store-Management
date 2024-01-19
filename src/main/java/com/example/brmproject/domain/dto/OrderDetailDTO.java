package com.example.brmproject.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {

    private int id;

    private Integer bookId;
    private Integer orderId;
    private String review;
    private Integer rating;

    private boolean rated;
    private String validReview;
    private BookDTO bookByBookId;
    private OrdersDTO ordersByOrderId;
    private Integer bookDetailId;
    private boolean lost;

    public boolean isRated() {
        return rated;
    }

    public void setRated(boolean rated) {
        this.rated = rated;
    }
}
