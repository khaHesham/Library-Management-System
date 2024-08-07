package com.librarymanagement.API.service;

import com.librarymanagement.API.dto.BookDTO;
import java.util.List;


public interface BookService {
    public List<BookDTO> getAllBooks();
    public BookDTO getBookById(Long id);
    public BookDTO addBook(BookDTO book);
    public BookDTO updateBook(Long id, BookDTO book);
    public boolean deleteBook(Long id);
}
