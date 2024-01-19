package com.example.brmproject.repositories;

import com.example.brmproject.domain.entities.BookshelfCaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookshelfCaseEntityRepository extends JpaRepository<BookshelfCaseEntity, Integer> {
    @Query("select c from BookshelfCaseEntity c where c.bookshelfCode =:code ")
    List<BookshelfCaseEntity> validBookCaseByCode(String code);
}