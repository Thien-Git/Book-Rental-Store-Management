package com.example.brmproject.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "category_book", schema = "brmproject", catalog = "")
public class CategoryBookEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "category_id", nullable = true,insertable = false, updatable = false)
    private Integer categoryId;
    @Basic
    @Column(name = "book_id", nullable = true,insertable = false, updatable = false)
    private Integer bookId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private CategoryEntity categoryByBookId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private BookEntity bookByBookId;




    public CategoryEntity getCategoryByBookId() {
        return categoryByBookId;
    }

    public void setCategoryByBookId(CategoryEntity categoryByBookId) {
        this.categoryByBookId = categoryByBookId;
    }

    public BookEntity getBookByBookId() {
        return bookByBookId;
    }

    public void setBookByBookId(BookEntity bookByBookId) {
        this.bookByBookId = bookByBookId;
    }
}
