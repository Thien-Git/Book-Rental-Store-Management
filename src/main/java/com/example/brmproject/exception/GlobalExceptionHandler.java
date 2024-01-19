package com.example.brmproject.exception;


import com.example.brmproject.exception.order.OrderNotFoundException;
import com.example.brmproject.exception.orderDetail.OrderDetailNotFoundException;
import com.example.brmproject.exception.reviewRating.ReviewRatingNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(OrderDetailNotFoundException.class)
    public String handleOrderDetailNotFoundException(OrderDetailNotFoundException exception,
                                                     Model model
    ){
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(HttpStatus.NOT_FOUND.value());
        errorObject.setMessage(exception.getMessage());
        errorObject.setTimestamp(new Date());

//        return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.NOT_FOUND);
        model.addAttribute("error", errorObject);
        return "/customerTemplate/error/error";
    }

    @ExceptionHandler(ReviewRatingNotFoundException.class)
    public String handleReviewRatingNotFoundException(ReviewRatingNotFoundException exception,
                                                     Model model
    ){
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(HttpStatus.NOT_FOUND.value());
        errorObject.setMessage(exception.getMessage());
        errorObject.setTimestamp(new Date());
        model.addAttribute("error", errorObject);

        return "/customerTemplate/error/error";
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public String handleOrderNotFoundException(OrderNotFoundException exception,
                                                      Model model
    ){
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(HttpStatus.NOT_FOUND.value());
        errorObject.setMessage(exception.getMessage());
        errorObject.setTimestamp(new Date());
        model.addAttribute("error", errorObject);

        return "/customerTemplate/error/error";
    }
}
