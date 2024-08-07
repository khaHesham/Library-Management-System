package com.librarymanagement.API.service.impl;

import com.librarymanagement.API.dto.BookDTO;
import com.librarymanagement.API.entity.Book;
import com.librarymanagement.API.mappers.BookMapper;
import com.librarymanagement.API.repository.BookRepository;
import com.librarymanagement.API.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookMapper bookMapper;

    @Override
    public List<BookDTO> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return bookMapper.toDTOs(books);
    }
    @Override
    @Cacheable(value = "books", key = "#id")
    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id).orElse(null);
        return bookMapper.toDTO(book);
    }

    @Override
    public BookDTO addBook(BookDTO book) {
        Book newBook = bookRepository.save(bookMapper.toEntity(book));
        return bookMapper.toDTO(newBook);
    }

    @Override
    @CachePut(value = "books", key = "#id")
    public BookDTO updateBook(Long id, BookDTO book) {
        if(!bookRepository.existsById(id)){
            return null;
        }
        Book updatedBook = bookRepository.save(bookMapper.toEntity(book));
        return bookMapper.toDTO(updatedBook);
    }

    @Override
    @CacheEvict(value = "books", key = "#id")
    public boolean deleteBook(Long id) {
        if(!bookRepository.existsById(id)){
            return false;
        }
        bookRepository.deleteById(id);
        return true;
    }
}
