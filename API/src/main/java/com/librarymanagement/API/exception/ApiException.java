package com.librarymanagement.API.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public abstract class ApiException extends RuntimeException {
    private final String message;
    private final HttpStatus status;
    public ApiException(String message, HttpStatus status) {
        super(message);
        this.message = message;
        this.status = status;
    }

}