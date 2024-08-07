package com.librarymanagement.API.exception;

import org.springframework.http.HttpStatus;

public class BookNotFoundException extends ApiException {
    public BookNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}