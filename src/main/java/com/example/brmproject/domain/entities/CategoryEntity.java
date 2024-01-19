package com.example.brmproject.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "category", schema = "brmproject", catalog = "")
public class CategoryEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "name", nullable = true, length = 100)
    private String name;
    @Basic
    @Column(name = "description", nullable = true, length = 225)
    private String description;
    @OneToMany(mappedBy = "categoryByBookId",cascade = CascadeType.ALL,orphanRemoval = true)
    private Collection<CategoryBookEntity> categoryBooksById;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryEntity that = (CategoryEntity) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }

    public Collection<CategoryBookEntity> getCategoryBooksById() {
        return categoryBooksById;
    }

    public void setCategoryBooksById(Collection<CategoryBookEntity> categoryBooksById) {
        this.categoryBooksById = categoryBooksById;
    }
}
