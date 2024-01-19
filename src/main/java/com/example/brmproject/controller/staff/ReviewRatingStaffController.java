package com.example.brmproject.controller.staff;

import com.example.brmproject.domain.dto.OrderDetailDTO;
import com.example.brmproject.domain.dto.ReviewRatingDTO;
import com.example.brmproject.service.imp.ReviewRatingServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/staff")
@RequiredArgsConstructor
public class ReviewRatingStaffController {
    private final ReviewRatingServiceImp reviewRatingServiceImp;

    @GetMapping("/review-rating")
    public String getReviewRating(@RequestParam("page") int page, Model model) {
        List<OrderDetailDTO> listReview = reviewRatingServiceImp.getAllReviewRating(page);

        int allReviewPending = reviewRatingServiceImp.countAllReviewRatingPending();
        int totalPages = allReviewPending / 10;

        if(allReviewPending % 10 > 0 ) {
            totalPages += 1;
        }

        model.addAttribute("listReview", listReview);
        model.addAttribute("amountOfReview", listReview.size());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);

        return "/adminTemplate/reviewManage/reviewManage";
    }

    @GetMapping("/update-review-rating")
    public String updateReviewRating(@RequestParam("orderDetailId") int orderDetailId,
                                     @RequestParam("valid") String validReview) {
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        orderDetailDTO.setValidReview(validReview);
        reviewRatingServiceImp.updateValidReviewRating(orderDetailId, orderDetailDTO);

        return "redirect:/staff/review-rating?page=1";
    }
}
