package com.librarymanagement.API.exception.patron;

import com.librarymanagement.API.exception.ApiException;
import org.springframework.http.HttpStatus;

public class InvalidPatronException extends ApiException {
    public InvalidPatronException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}