package com.librarymanagement.API.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "publication_year", nullable = false)
    private int publicationYear;

    @Column(name = "isbn", nullable = false)
    private String isbn;

    @OneToMany(mappedBy = "book",cascade = CascadeType.ALL)
    private List<BorrowingRecord> borrowingRecords = new ArrayList<BorrowingRecord>();
}