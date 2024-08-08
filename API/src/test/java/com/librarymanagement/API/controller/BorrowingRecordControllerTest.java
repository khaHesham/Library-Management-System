package com.librarymanagement.API.controller;

import com.librarymanagement.API.dto.BorrowingRecordDTO;
import com.librarymanagement.API.exception.book.BookAlreadyBorrowedException;
import com.librarymanagement.API.exception.book.BookNotFoundException;
import com.librarymanagement.API.exception.patron.PatronNotFoundException;
import com.librarymanagement.API.service.BorrowingRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class BorrowingRecordControllerTest {

    @Mock
    private BorrowingRecordService borrowingRecordService;

    @InjectMocks
    private BorrowingRecordController borrowingRecordController;

    private BorrowingRecordDTO borrowingRecordDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        borrowingRecordDTO = new BorrowingRecordDTO(
                "2024-08-08",
                null // No return date yet
        );
    }

    @Test
    void borrowBook_ShouldReturnCreatedStatus_WhenBookIsBorrowedSuccessfully() {
        when(borrowingRecordService.borrowBook(anyLong(), anyLong())).thenReturn(borrowingRecordDTO);

        ResponseEntity<BorrowingRecordDTO> response = borrowingRecordController.borrowBook(1L, 1L);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("2024-08-08", response.getBody().getBorrowDate());
        verify(borrowingRecordService, times(1)).borrowBook(1L, 1L);
    }

    @Test
    void borrowBook_ShouldThrowBookNotFoundException_WhenBookDoesNotExist() {
        when(borrowingRecordService.borrowBook(anyLong(), anyLong())).thenThrow(new BookNotFoundException("Book not found"));

        assertThrows(BookNotFoundException.class, () -> borrowingRecordController.borrowBook(1L, 1L));

        verify(borrowingRecordService, times(1)).borrowBook(1L, 1L);
    }

    @Test
    void borrowBook_ShouldThrowPatronNotFoundException_WhenPatronDoesNotExist() {
        when(borrowingRecordService.borrowBook(anyLong(), anyLong())).thenThrow(new PatronNotFoundException("Patron not found"));

        assertThrows(PatronNotFoundException.class, () -> borrowingRecordController.borrowBook(1L, 1L));

        verify(borrowingRecordService, times(1)).borrowBook(1L, 1L);
    }

    @Test
    void borrowBook_ShouldThrowBookAlreadyBorrowedException_WhenBookIsAlreadyBorrowed() {
        when(borrowingRecordService.borrowBook(anyLong(), anyLong())).thenThrow(new BookAlreadyBorrowedException("Book already borrowed"));

        assertThrows(BookAlreadyBorrowedException.class, () -> borrowingRecordController.borrowBook(1L, 1L));

        verify(borrowingRecordService, times(1)).borrowBook(1L, 1L);
    }

    @Test
    void returnBook_ShouldReturnOkStatus_WhenBookIsReturnedSuccessfully() {
        when(borrowingRecordService.updateReturnDate(anyLong(), anyLong())).thenReturn(borrowingRecordDTO);

        ResponseEntity<BorrowingRecordDTO> response = borrowingRecordController.returnBook(1L, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("2024-08-08", response.getBody().getBorrowDate());
        verify(borrowingRecordService, times(1)).updateReturnDate(1L, 1L);
    }

    @Test
    void returnBook_ShouldThrowBookNotFoundException_WhenBookDoesNotExist() {
        when(borrowingRecordService.updateReturnDate(anyLong(), anyLong())).thenThrow(new BookNotFoundException("Book not found"));

        assertThrows(BookNotFoundException.class, () -> borrowingRecordController.returnBook(1L, 1L));

        verify(borrowingRecordService, times(1)).updateReturnDate(1L, 1L);
    }

    @Test
    void returnBook_ShouldThrowPatronNotFoundException_WhenPatronDidNotBorrowBook() {
        when(borrowingRecordService.updateReturnDate(anyLong(), anyLong())).thenThrow(new PatronNotFoundException("Patron not found"));

        assertThrows(PatronNotFoundException.class, () -> borrowingRecordController.returnBook(1L, 1L));

        verify(borrowingRecordService, times(1)).updateReturnDate(1L, 1L);
    }
}
