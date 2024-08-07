package com.librarymanagement.API.service;

import com.librarymanagement.API.dto.BorrowingRecordDTO;
import org.springframework.stereotype.Service;


public interface BorrowingRecordService {

    BorrowingRecordDTO borrowBook(Long bookId, Long patronId);

    BorrowingRecordDTO updateReturnDate(Long bookId, Long patronId);

}
