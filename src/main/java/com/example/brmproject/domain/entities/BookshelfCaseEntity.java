package com.example.brmproject.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "bookshelf_case", schema = "brmproject", catalog = "")
public class BookshelfCaseEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "bookshelf_code", nullable = false, length = 10)
    private String bookshelfCode;
    @Basic
    @Column(name = "case_number", nullable = true)
    private Integer caseNumber;
    @Basic
    @Column(name = "capacity", nullable = true)
    private Integer capacity;
    @Basic
    @Column(name = "available_blank", nullable = true)
    private Integer availableBlank;
    @OneToMany(mappedBy = "bookshelfCaseByBookshelfId",cascade = CascadeType.ALL,orphanRemoval = true)
    private Collection<BookEntity> booksById;




    public Collection<BookEntity> getBooksById() {
        return booksById;
    }

    public void setBooksById(Collection<BookEntity> booksById) {
        this.booksById = booksById;
    }
}
