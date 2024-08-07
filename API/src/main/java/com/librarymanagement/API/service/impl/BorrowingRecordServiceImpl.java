package com.librarymanagement.API.service.impl;

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
import com.librarymanagement.API.service.BorrowingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class BorrowingRecordServiceImpl implements BorrowingRecordService {

    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PatronRepository patronRepository;

    @Autowired
    private BorrowingRecordMapper borrowingRecordMapper;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    @Transactional
    public BorrowingRecordDTO borrowBook(Long bookId, Long patronId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + bookId));

        Patron patron = patronRepository.findById(patronId)
                .orElseThrow(() -> new PatronNotFoundException("Patron not found with id: " + patronId));

        BorrowingRecord existingRecord = borrowingRecordRepository.findByBookIdAndReturnDateIsNull(bookId);
        if (existingRecord != null) {
            throw new BookAlreadyBorrowedException("Book with id: " + bookId + " is already borrowed.");
        }

        // Create a new borrowing record
        BorrowingRecord borrowingRecord = new BorrowingRecord();
        borrowingRecord.setBook(book);
        borrowingRecord.setPatron(patron);
        borrowingRecord.setBorrowDate(LocalDate.now().format(DATE_FORMATTER));
        borrowingRecord.setReturnDate(null); // Return date will be set when the book is returned

        BorrowingRecord savedBorrowingRecord = borrowingRecordRepository.save(borrowingRecord);

        return borrowingRecordMapper.toDTO(savedBorrowingRecord);
    }

    @Override
    @Transactional
    public BorrowingRecordDTO updateReturnDate(Long bookId, Long patronId) {
        BorrowingRecord borrowingRecord = borrowingRecordRepository.findByBookIdAndReturnDateIsNull(bookId);
        if (borrowingRecord == null) {
            throw new BookNotFoundException("Book with id: " + bookId + " is not borrowed.");
        }
        if(!borrowingRecord.getPatron().getId().equals(patronId)){
            throw new PatronNotFoundException("Patron with id: " + patronId + " did not borrow the book.");
        }
        // Update the return date
        borrowingRecord.setReturnDate(LocalDate.now().format(DATE_FORMATTER));

        BorrowingRecord savedBorrowingRecord = borrowingRecordRepository.save(borrowingRecord);

        return borrowingRecordMapper.toDTO(savedBorrowingRecord);
    }
}
