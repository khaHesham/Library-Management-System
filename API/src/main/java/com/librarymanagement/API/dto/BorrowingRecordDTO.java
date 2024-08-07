package com.librarymanagement.API.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.librarymanagement.API.entity.BorrowingRecord}
 */
@Value
@Setter
@Getter
public class BorrowingRecordDTO implements Serializable {

    @NotBlank(message = "Borrow date is mandatory")
    @Pattern(
            regexp = "\\d{4}-\\d{2}-\\d{2}",  // YYYY-MM-DD format
            message = "Borrow date must be in the format YYYY-MM-DD"
    )
    String borrowDate;

    @Pattern(
            regexp = "\\d{4}-\\d{2}-\\d{2}",  // YYYY-MM-DD format
            message = "Return date must be in the format YYYY-MM-DD"
    )
    String returnDate;
}
