package com.librarymanagement.API.exception.book;

import com.librarymanagement.API.exception.ApiException;
import org.springframework.http.HttpStatus;

public class InvalidBookException extends ApiException {
    public InvalidBookException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}