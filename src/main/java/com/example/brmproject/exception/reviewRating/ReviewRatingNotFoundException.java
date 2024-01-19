package com.example.brmproject.exception.reviewRating;

import java.io.Serial;

public class ReviewRatingNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ReviewRatingNotFoundException(String message) {
        super(message);
    }
}
