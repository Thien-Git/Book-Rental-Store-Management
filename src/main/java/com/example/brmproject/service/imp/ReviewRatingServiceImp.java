package com.example.brmproject.service.imp;

import com.example.brmproject.domain.dto.OrderDetailDTO;
import com.example.brmproject.domain.dto.ReviewRatingDTO;
import com.example.brmproject.domain.dto.UserReviewDTO;
import com.example.brmproject.domain.entities.OrderDetailEntity;
import com.example.brmproject.exception.orderDetail.OrderDetailNotFoundException;
import com.example.brmproject.repositories.OrderDetailEntityRepository;
import com.example.brmproject.service.ReviewRatingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewRatingServiceImp implements ReviewRatingService {
    private ModelMapper modelMapper;

    private OrderDetailEntityRepository orderDetailEntityRepository;
@Autowired
    public ReviewRatingServiceImp(OrderDetailEntityRepository orderDetailEntityRepository, ModelMapper modelMapper) {
        this.orderDetailEntityRepository = orderDetailEntityRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void createReviewRating(OrderDetailDTO reviewObj, int orderDetailId) {
        Optional<OrderDetailEntity> orderDetailEntityDb = orderDetailEntityRepository.findById(orderDetailId);

        if(orderDetailEntityDb.isEmpty()) {
            throw new OrderDetailNotFoundException("Order detail not found");
        }

        OrderDetailEntity orderDetailEntity = orderDetailEntityDb.get();

        if(!orderDetailEntity.isRated()) {
            if(reviewObj.getRating() >= 4) {
                orderDetailEntity.setValidReview("VALID");
            }

            orderDetailEntity.setRating(reviewObj.getRating());
            orderDetailEntity.setReview(reviewObj.getReview());
            orderDetailEntity.setRated(true);
            orderDetailEntityRepository.save(orderDetailEntity);
        }
    }

    @Override
    public List<OrderDetailDTO> getAllReviewRating(int page) {
        int limit = 10;
        int skip = (page - 1) * limit;

        List<OrderDetailEntity> listReview = orderDetailEntityRepository.
                findByValidReviewStatus("PENDING", PageRequest.of(skip, limit));

        System.out.println(listReview.size());
        return listReview.stream().map((review) -> mapToDTO(review)).toList();
    }

    @Override
    public void updateValidReviewRating(int orderDetailId, OrderDetailDTO orderDetailDTO) {
        OrderDetailEntity orderDetailEntity = orderDetailEntityRepository.findById(orderDetailId).get();
        orderDetailEntity.setValidReview(orderDetailDTO.getValidReview());
        orderDetailEntityRepository.save(orderDetailEntity);
    }

    @Override
    public int countAllReviewRatingPending() {
        return orderDetailEntityRepository.countAllReviewRating();
    }

    @Override
    public ReviewRatingDTO getReviewRatingByBook(int bookId) {
        List<OrderDetailEntity> listReview = orderDetailEntityRepository.findByBookId(bookId);

        if(listReview.isEmpty()) {
            return new ReviewRatingDTO();
        }

        List<OrderDetailEntity> listFilter = listReview.stream().filter((review) -> review.getValidReview().equals("VALID")).toList();

        if(listFilter.isEmpty()) {
            return new ReviewRatingDTO();
        }

        double avrRating = 0;
        avrRating = listFilter.stream().reduce(0.0, (acc, review) -> acc + review.getRating(), (acc1, acc2) -> acc1 + acc2);

        List<UserReviewDTO> listReviewString = listFilter.stream().map((review) -> {
            UserReviewDTO userReviewDTO = new UserReviewDTO();
            String userName = review.getOrdersByOrderId().getCustomerByCustomerId().getName();
            int userId = review.getOrdersByOrderId().getCustomerId();

            userReviewDTO.setUsername(userName);
            userReviewDTO.setId(userId);
            userReviewDTO.setReview(review.getReview());
            userReviewDTO.setRating(review.getRating());

            return userReviewDTO;
        }).toList();

        ReviewRatingDTO reviewRatingDTO = new ReviewRatingDTO();
        reviewRatingDTO.setAvrRating(avrRating / listFilter.size());
        reviewRatingDTO.setListReview(listReviewString);

        return reviewRatingDTO;
    }

    private OrderDetailEntity mapToEntity(OrderDetailDTO orderDetailDTO) {
        return modelMapper.map(orderDetailDTO, OrderDetailEntity.class);
    }

    private OrderDetailDTO mapToDTO(OrderDetailEntity orderDetailEntity) {
        return modelMapper.map(orderDetailEntity, OrderDetailDTO.class);
    }

    ///


}
