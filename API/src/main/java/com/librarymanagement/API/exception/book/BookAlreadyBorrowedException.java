package com.librarymanagement.API.exception.book;

import com.librarymanagement.API.exception.ApiException;
import org.springframework.http.HttpStatus;

public class BookAlreadyBorrowedException extends ApiException {
    public BookAlreadyBorrowedException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}