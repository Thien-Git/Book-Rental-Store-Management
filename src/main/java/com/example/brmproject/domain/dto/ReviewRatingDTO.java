package com.example.brmproject.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRatingDTO {
    private double avrRating = 0;
    private List<UserReviewDTO> listReview = new ArrayList<>();
}
