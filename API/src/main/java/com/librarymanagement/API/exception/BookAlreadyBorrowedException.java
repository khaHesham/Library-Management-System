package com.librarymanagement.API.exception;

import org.springframework.http.HttpStatus;

public class BookAlreadyBorrowedException extends ApiException {
    public BookAlreadyBorrowedException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}