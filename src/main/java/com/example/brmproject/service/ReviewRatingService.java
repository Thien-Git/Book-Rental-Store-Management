package com.example.brmproject.service;

import com.example.brmproject.domain.dto.OrderDetailDTO;
import com.example.brmproject.domain.dto.ReviewRatingDTO;

import java.util.List;

public interface ReviewRatingService {
    void createReviewRating(OrderDetailDTO reviewObj, int orderDetailId);
    List<OrderDetailDTO> getAllReviewRating(int page);

    void updateValidReviewRating(int orderDetailId, OrderDetailDTO orderDetailDTO);

    ReviewRatingDTO getReviewRatingByBook(int bookId);

    int countAllReviewRatingPending();
}
