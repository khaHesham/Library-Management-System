package com.librarymanagement.API.dto;

import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.librarymanagement.API.entity.Patron}
 */
@Value
@Setter
@Getter
public class PatronDTO implements Serializable {

    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    String name;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    String email;

    @NotBlank(message = "Phone number is mandatory")
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Phone number is invalid")
    String phoneNumber;

    @NotBlank(message = "Address is mandatory")
    @Size(max = 100, message = "Address must not exceed 100 characters")
    String address;

    @NotBlank(message = "City is mandatory")
    @Size(max = 50, message = "City must not exceed 50 characters")
    String city;

    @NotBlank(message = "State is mandatory")
    @Size(max = 50, message = "State must not exceed 50 characters")
    String state;

    @NotBlank(message = "Zip code is mandatory")
    @Pattern(regexp = "^[0-9]{5}(?:-[0-9]{4})?$", message = "Zip code must be valid")
    String zipCode;
}
