package com.librarymanagement.API.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.librarymanagement.API.entity.Book}
 */
@Value
@Setter
@Getter
public class BookDTO implements Serializable {
    @NotBlank(message = "Title is mandatory")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    String title;

    @NotBlank(message = "Author is mandatory")
    @Size(max = 50, message = "Author must not exceed 50 characters")
    String author;

    @Positive(message = "Publication year must be a positive number")
    int publicationYear;

    @NotBlank(message = "ISBN is mandatory")
    @Pattern(
            regexp = "^(97(8|9))?\\d{9}(\\d|X)$",
            message = "ISBN must be a valid 10 or 13 digit number"
    )
    String isbn;
}