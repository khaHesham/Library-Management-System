package com.librarymanagement.API.exception.book;

import com.librarymanagement.API.exception.ApiException;
import org.springframework.http.HttpStatus;

public class BookNotFoundException extends ApiException {
    public BookNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}