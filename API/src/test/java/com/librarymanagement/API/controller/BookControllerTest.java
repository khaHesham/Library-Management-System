package com.librarymanagement.API.controller;

import com.librarymanagement.API.dto.BookDTO;
import com.librarymanagement.API.exception.book.BookNotFoundException;
import com.librarymanagement.API.exception.book.InvalidBookException;
import com.librarymanagement.API.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private BookDTO bookDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        bookDTO = new BookDTO(
                "Test Title",
                "Test Author",
                2024,
                "9781234567897"
        );
    }

    @Test
    void getAllBooks_ShouldReturnListOfBooks() {
        List<BookDTO> books = Arrays.asList(bookDTO);
        when(bookService.getAllBooks()).thenReturn(books);

        ResponseEntity<List<BookDTO>> response = bookController.getAllBooks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void getBookById_ShouldReturnBook_WhenBookExists() {
        when(bookService.getBookById(anyLong())).thenReturn(bookDTO);

        ResponseEntity<BookDTO> response = bookController.getBookById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Title", response.getBody().getTitle());
        verify(bookService, times(1)).getBookById(1L);
    }

    @Test
    void getBookById_ShouldThrowBookNotFoundException_WhenBookDoesNotExist() {
        when(bookService.getBookById(anyLong())).thenReturn(null);

        assertThrows(BookNotFoundException.class, () -> bookController.getBookById(1L));

        verify(bookService, times(1)).getBookById(1L);
    }

    @Test
    void addBook_ShouldReturnCreatedStatus_WhenBookIsValid() {
        when(bookService.addBook(any(BookDTO.class))).thenReturn(bookDTO);

        ResponseEntity<BookDTO> response = bookController.addBook(bookDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Title", response.getBody().getTitle());
        verify(bookService, times(1)).addBook(any(BookDTO.class));
    }

    @Test
    void addBook_ShouldThrowInvalidBookException_WhenBookIsInvalid() {
        BookDTO invalidBookDTO = new BookDTO(
                "", // Invalid title
                "Test Author",
                2024,
                "9781234567897"
        );

        assertThrows(InvalidBookException.class, () -> bookController.addBook(invalidBookDTO));

        verify(bookService, never()).addBook(any(BookDTO.class));
    }

    @Test
    void updateBook_ShouldReturnUpdatedBook_WhenBookIsValid() {
        when(bookService.updateBook(anyLong(), any(BookDTO.class))).thenReturn(bookDTO);

        ResponseEntity<BookDTO> response = bookController.updateBook(1L, bookDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Title", response.getBody().getTitle());
        verify(bookService, times(1)).updateBook(anyLong(), any(BookDTO.class));
    }

    @Test
    void updateBook_ShouldThrowBookNotFoundException_WhenBookDoesNotExist() {
        when(bookService.updateBook(anyLong(), any(BookDTO.class))).thenReturn(null);

        assertThrows(BookNotFoundException.class, () -> bookController.updateBook(1L, bookDTO));

        verify(bookService, times(1)).updateBook(anyLong(), any(BookDTO.class));
    }

    @Test
    void deleteBook_ShouldReturnNoContentStatus_WhenBookIsDeleted() {
        when(bookService.deleteBook(anyLong())).thenReturn(true);

        ResponseEntity<Void> response = bookController.deleteBook(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(bookService, times(1)).deleteBook(1L);
    }

    @Test
    void deleteBook_ShouldThrowBookNotFoundException_WhenBookDoesNotExist() {
        when(bookService.deleteBook(anyLong())).thenReturn(false);

        assertThrows(BookNotFoundException.class, () -> bookController.deleteBook(1L));

        verify(bookService, times(1)).deleteBook(1L);
    }
}
