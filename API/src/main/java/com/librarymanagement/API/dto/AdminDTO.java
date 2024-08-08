package com.librarymanagement.API.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.librarymanagement.API.entity.Admin}
 */
@Value
public class AdminDTO implements Serializable {
    @NotBlank(message = "Username is mandatory")
    @Size(min = 3, max = 50, message = "Username or Email must be between 3 and 50 characters")
    String username;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, max = 100, message = "Password must be at least 8 characters long")
    String password;
}