package com.librarymanagement.API.service;

import com.librarymanagement.API.dto.BorrowingRecordDTO;
import com.librarymanagement.API.entity.Book;
import com.librarymanagement.API.entity.BorrowingRecord;
import com.librarymanagement.API.entity.Patron;
import com.librarymanagement.API.exception.BookAlreadyBorrowedException;
import com.librarymanagement.API.exception.book.BookNotFoundException;
import com.librarymanagement.API.exception.patron.PatronNotFoundException;
import com.librarymanagement.API.mappers.BorrowingRecordMapper;
import com.librarymanagement.API.repository.BookRepository;
import com.librarymanagement.API.repository.BorrowingRecordRepository;
import com.librarymanagement.API.repository.PatronRepository;
import com.librarymanagement.API.service.impl.BorrowingRecordServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class BorrowingRecordServiceImplTest {

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PatronRepository patronRepository;

    @Mock
    private BorrowingRecordMapper borrowingRecordMapper;

    @InjectMocks
    private BorrowingRecordServiceImpl borrowingRecordService;

    private Book book;
    private Patron patron;
    private BorrowingRecord borrowingRecord;
    private BorrowingRecordDTO borrowingRecordDTO;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");

        patron = new Patron();
        patron.setId(1L);
        patron.setName("Test Patron");

        borrowingRecord = new BorrowingRecord();
        borrowingRecord.setId(1L);
        borrowingRecord.setBook(book);
        borrowingRecord.setPatron(patron);
        borrowingRecord.setBorrowDate(LocalDate.now().format(DATE_FORMATTER));

        borrowingRecordDTO = new BorrowingRecordDTO(borrowingRecord.getBorrowDate(), borrowingRecord.getReturnDate());
    }

    @Test
    void borrowBook_ShouldReturnBorrowingRecordDTO_WhenBookIsNotAlreadyBorrowed() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(patronRepository.findById(anyLong())).thenReturn(Optional.of(patron));
        when(borrowingRecordRepository.findByBookIdAndReturnDateIsNull(anyLong())).thenReturn(null);
        when(borrowingRecordRepository.save(any(BorrowingRecord.class))).thenReturn(borrowingRecord);
        when(borrowingRecordMapper.toDTO(any(BorrowingRecord.class))).thenReturn(borrowingRecordDTO);

        BorrowingRecordDTO result = borrowingRecordService.borrowBook(1L, 1L);

        assertNotNull(result);
        assertEquals(borrowingRecord.getBorrowDate(), result.getBorrowDate());
        verify(bookRepository, times(1)).findById(1L);
        verify(patronRepository, times(1)).findById(1L);
        verify(borrowingRecordRepository, times(1)).findByBookIdAndReturnDateIsNull(1L);
        verify(borrowingRecordRepository, times(1)).save(any(BorrowingRecord.class));
        verify(borrowingRecordMapper, times(1)).toDTO(any(BorrowingRecord.class));
    }

    @Test
    void borrowBook_ShouldThrowBookNotFoundException_WhenBookDoesNotExist() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> borrowingRecordService.borrowBook(1L, 1L));

        verify(bookRepository, times(1)).findById(1L);
        verify(patronRepository, never()).findById(anyLong());
        verify(borrowingRecordRepository, never()).findByBookIdAndReturnDateIsNull(anyLong());
        verify(borrowingRecordRepository, never()).save(any(BorrowingRecord.class));
        verify(borrowingRecordMapper, never()).toDTO(any(BorrowingRecord.class));
    }

    @Test
    void borrowBook_ShouldThrowPatronNotFoundException_WhenPatronDoesNotExist() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(patronRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(PatronNotFoundException.class, () -> borrowingRecordService.borrowBook(1L, 1L));

        verify(bookRepository, times(1)).findById(1L);
        verify(patronRepository, times(1)).findById(1L);
        verify(borrowingRecordRepository, never()).findByBookIdAndReturnDateIsNull(anyLong());
        verify(borrowingRecordRepository, never()).save(any(BorrowingRecord.class));
        verify(borrowingRecordMapper, never()).toDTO(any(BorrowingRecord.class));
    }

    @Test
    void borrowBook_ShouldThrowBookAlreadyBorrowedException_WhenBookIsAlreadyBorrowed() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(patronRepository.findById(anyLong())).thenReturn(Optional.of(patron));
        when(borrowingRecordRepository.findByBookIdAndReturnDateIsNull(anyLong())).thenReturn(borrowingRecord);

        assertThrows(BookAlreadyBorrowedException.class, () -> borrowingRecordService.borrowBook(1L, 1L));

        verify(bookRepository, times(1)).findById(1L);
        verify(patronRepository, times(1)).findById(1L);
        verify(borrowingRecordRepository, times(1)).findByBookIdAndReturnDateIsNull(1L);
        verify(borrowingRecordRepository, never()).save(any(BorrowingRecord.class));
        verify(borrowingRecordMapper, never()).toDTO(any(BorrowingRecord.class));
    }

    @Test
    void updateReturnDate_ShouldReturnBorrowingRecordDTO_WhenBorrowingRecordExists() {
        when(borrowingRecordRepository.findByBookIdAndReturnDateIsNull(anyLong())).thenReturn(borrowingRecord);
        when(borrowingRecordRepository.save(any(BorrowingRecord.class))).thenReturn(borrowingRecord);
        when(borrowingRecordMapper.toDTO(any(BorrowingRecord.class))).thenReturn(borrowingRecordDTO);

        BorrowingRecordDTO result = borrowingRecordService.updateReturnDate(1L, 1L);

        assertNotNull(result);
        assertEquals(borrowingRecord.getBorrowDate(), result.getBorrowDate());
        verify(borrowingRecordRepository, times(1)).findByBookIdAndReturnDateIsNull(1L);
        verify(borrowingRecordRepository, times(1)).save(any(BorrowingRecord.class));
        verify(borrowingRecordMapper, times(1)).toDTO(any(BorrowingRecord.class));
    }

    @Test
    void updateReturnDate_ShouldThrowBookNotFoundException_WhenBorrowingRecordDoesNotExist() {
        when(borrowingRecordRepository.findByBookIdAndReturnDateIsNull(anyLong())).thenReturn(null);

        assertThrows(BookNotFoundException.class, () -> borrowingRecordService.updateReturnDate(1L, 1L));

        verify(borrowingRecordRepository, times(1)).findByBookIdAndReturnDateIsNull(1L);
        verify(borrowingRecordRepository, never()).save(any(BorrowingRecord.class));
        verify(borrowingRecordMapper, never()).toDTO(any(BorrowingRecord.class));
    }

    @Test
    void updateReturnDate_ShouldThrowPatronNotFoundException_WhenPatronDidNotBorrowBook() {
        Patron anotherPatron = new Patron();
        anotherPatron.setId(2L);

        borrowingRecord.setPatron(anotherPatron);

        when(borrowingRecordRepository.findByBookIdAndReturnDateIsNull(anyLong())).thenReturn(borrowingRecord);

        assertThrows(PatronNotFoundException.class, () -> borrowingRecordService.updateReturnDate(1L, 1L));

        verify(borrowingRecordRepository, times(1)).findByBookIdAndReturnDateIsNull(1L);
        verify(borrowingRecordRepository, never()).save(any(BorrowingRecord.class));
        verify(borrowingRecordMapper, never()).toDTO(any(BorrowingRecord.class));
    }
}