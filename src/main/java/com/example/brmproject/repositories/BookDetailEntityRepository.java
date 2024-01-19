package com.example.brmproject.repositories;

import com.example.brmproject.domain.entities.BookDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookDetailEntityRepository extends JpaRepository<BookDetailEntity, Integer> {
    @Query("select bd from BookDetailEntity bd where bd.bookId = :bookId")
    List<BookDetailEntity> findByBookId(Integer bookId);

}