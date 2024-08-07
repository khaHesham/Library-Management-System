package com.librarymanagement.API.exception.patron;

import com.librarymanagement.API.exception.ApiException;
import org.springframework.http.HttpStatus;

public class PatronNotFoundException extends ApiException {
    public PatronNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
