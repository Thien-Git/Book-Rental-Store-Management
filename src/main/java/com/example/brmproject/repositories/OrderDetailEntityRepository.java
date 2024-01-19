package com.example.brmproject.repositories;

import com.example.brmproject.domain.entities.OrderDetailEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailEntityRepository extends JpaRepository<OrderDetailEntity, Integer> {
    List<OrderDetailEntity> findByOrderId(int orderId);

    @Query("SELECT o FROM OrderDetailEntity o WHERE o.validReview = ?1 AND o.rating <= 3 " +
            "ORDER BY o.id DESC")
    List<OrderDetailEntity>  findByValidReviewStatus(String validReview, Pageable pageable);

    List<OrderDetailEntity> findByBookId(int bookId);

    @Query("SELECT COUNT(o) FROM OrderDetailEntity o WHERE o.validReview = 'PENDING' and o.rated = true")
    int countAllReviewRating();
}