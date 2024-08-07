package com.librarymanagement.API.exception;

import org.springframework.http.HttpStatus;

public class InvalidBookException extends ApiException {
    public InvalidBookException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}