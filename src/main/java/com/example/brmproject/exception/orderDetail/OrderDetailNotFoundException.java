package com.example.brmproject.exception.orderDetail;

import org.springframework.web.bind.annotation.ControllerAdvice;

import java.io.Serial;

public class OrderDetailNotFoundException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;

    public OrderDetailNotFoundException(String message) {
        super(message);
    }


}
