package com.librarymanagement.API.controller;

import com.librarymanagement.API.dto.BorrowingRecordDTO;
import com.librarymanagement.API.exception.GlobalExceptionHandler;
import com.librarymanagement.API.service.BorrowingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BorrowingRecordController extends GlobalExceptionHandler {

    @Autowired
    private BorrowingRecordService borrowingRecordService;

    @PostMapping("/borrow/{bookId}/patron/{patronId}")
    public ResponseEntity<BorrowingRecordDTO> borrowBook(@PathVariable Long bookId, @PathVariable Long patronId) {
        BorrowingRecordDTO borrowingRecord = borrowingRecordService.borrowBook(bookId, patronId);
        return new ResponseEntity<>(borrowingRecord, HttpStatus.CREATED);
    }

    @PutMapping("/return/{bookId}/patron/{patronId}")
    public ResponseEntity<BorrowingRecordDTO> returnBook(@PathVariable Long bookId, @PathVariable Long patronId) {
        BorrowingRecordDTO borrowingRecord = borrowingRecordService.updateReturnDate(bookId, patronId);
        return new ResponseEntity<>(borrowingRecord, HttpStatus.OK);
    }

}
