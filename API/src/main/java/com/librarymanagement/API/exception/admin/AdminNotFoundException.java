package com.librarymanagement.API.exception.admin;

import com.librarymanagement.API.exception.ApiException;
import org.springframework.http.HttpStatus;

public class AdminNotFoundException extends ApiException {
    public AdminNotFoundException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
