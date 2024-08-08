package com.librarymanagement.API.exception.admin;

import com.librarymanagement.API.exception.ApiException;
import org.springframework.http.HttpStatus;

public class AdminAlreadyExistsException extends ApiException {
    public AdminAlreadyExistsException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
