package com.librarymanagement.API.controller;

import com.librarymanagement.API.dto.BookDTO;
import com.librarymanagement.API.exception.BookNotFoundException;
import com.librarymanagement.API.exception.GlobalExceptionHandler;
import com.librarymanagement.API.exception.InvalidBookException;
import com.librarymanagement.API.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController extends GlobalExceptionHandler {
    @Autowired
    private BookService bookService;
    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> books = bookService.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        BookDTO book = bookService.getBookById(id);
        if (book == null) {
            throw new BookNotFoundException("Book not found with id: " + id);
        }
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BookDTO> addBook(@Valid @RequestBody BookDTO bookDto) {
        BookDTO book = bookService.addBook(bookDto);
        return new ResponseEntity<>(book, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id,@Valid @RequestBody BookDTO bookDto) {
        if (bookDto == null || bookDto.getTitle() == null || bookDto.getTitle().isEmpty()) {
            throw new InvalidBookException("Invalid book data provided.");
        }
        BookDTO book = bookService.updateBook(id, bookDto);
        if (book == null) {
            throw new BookNotFoundException("Cannot update, book not found with id: " + id);
        }
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        boolean deleted = bookService.deleteBook(id);
        if (!deleted) {
            throw new BookNotFoundException("Cannot delete, book not found with id: " + id);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
