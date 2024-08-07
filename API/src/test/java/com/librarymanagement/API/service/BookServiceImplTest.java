package com.librarymanagement.API.service;

import com.librarymanagement.API.dto.BookDTO;
import com.librarymanagement.API.entity.Book;
import com.librarymanagement.API.mappers.BookMapper;
import com.librarymanagement.API.repository.BookRepository;
import com.librarymanagement.API.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;
    private BookDTO bookDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");

        bookDTO = new BookDTO("Test Book", "Test Author", 2023, "1234567890");
    }

    @Test
    void getAllBooks_ShouldReturnBookDTOList() {
        List<Book> books = new ArrayList<>();
        books.add(book);

        List<BookDTO> bookDTOs = new ArrayList<>();
        bookDTOs.add(bookDTO);

        when(bookRepository.findAll()).thenReturn(books);
        when(bookMapper.toDTOs(books)).thenReturn(bookDTOs);

        List<BookDTO> result = bookService.getAllBooks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Book", result.get(0).getTitle());
        verify(bookRepository, times(1)).findAll();
        verify(bookMapper, times(1)).toDTOs(books);
    }

    @Test
    void getBookById_ShouldReturnBookDTO_WhenBookExists() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(bookMapper.toDTO(book)).thenReturn(bookDTO);

        BookDTO result = bookService.getBookById(1L);

        assertNotNull(result);
        assertEquals("Test Book", result.getTitle());
        verify(bookRepository, times(1)).findById(1L);
        verify(bookMapper, times(1)).toDTO(book);
    }

    @Test
    void getBookById_ShouldReturnNull_WhenBookDoesNotExist() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        BookDTO result = bookService.getBookById(1L);

        assertNull(result);
        verify(bookRepository, times(1)).findById(1L);
        verify(bookMapper, never()).toDTO(any(Book.class));
    }

    @Test
    void addBook_ShouldReturnBookDTO_WhenBookIsAdded() {
        when(bookMapper.toEntity(any(BookDTO.class))).thenReturn(book);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookMapper.toDTO(any(Book.class))).thenReturn(bookDTO);

        BookDTO result = bookService.addBook(bookDTO);

        assertNotNull(result);
        assertEquals("Test Book", result.getTitle());
        verify(bookMapper, times(1)).toEntity(bookDTO);
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).toDTO(book);
    }

    @Test
    void updateBook_ShouldReturnUpdatedBookDTO_WhenBookExists() {
        when(bookRepository.existsById(anyLong())).thenReturn(true);
        when(bookMapper.toEntity(any(BookDTO.class))).thenReturn(book);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookMapper.toDTO(any(Book.class))).thenReturn(bookDTO);

        BookDTO result = bookService.updateBook(1L, bookDTO);

        assertNotNull(result);
        assertEquals("Test Book", result.getTitle());
        verify(bookRepository, times(1)).existsById(1L);
        verify(bookMapper, times(1)).toEntity(bookDTO);
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).toDTO(book);
    }

    @Test
    void updateBook_ShouldReturnNull_WhenBookDoesNotExist() {
        when(bookRepository.existsById(anyLong())).thenReturn(false);

        BookDTO result = bookService.updateBook(1L, bookDTO);

        assertNull(result);
        verify(bookRepository, times(1)).existsById(1L);
        verify(bookMapper, never()).toEntity(any(BookDTO.class));
        verify(bookRepository, never()).save(any(Book.class));
        verify(bookMapper, never()).toDTO(any(Book.class));
    }

    @Test
    void deleteBook_ShouldReturnTrue_WhenBookExists() {
        when(bookRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(bookRepository).deleteById(anyLong());

        boolean result = bookService.deleteBook(1L);

        assertTrue(result);
        verify(bookRepository, times(1)).existsById(1L);
        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteBook_ShouldReturnFalse_WhenBookDoesNotExist() {
        when(bookRepository.existsById(anyLong())).thenReturn(false);

        boolean result = bookService.deleteBook(1L);

        assertFalse(result);
        verify(bookRepository, times(1)).existsById(1L);
        verify(bookRepository, never()).deleteById(anyLong());
    }
}