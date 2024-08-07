package com.librarymanagement.API.mappers;

import com.librarymanagement.API.dto.BookDTO;
import com.librarymanagement.API.dto.PatronDTO;
import com.librarymanagement.API.entity.Book;
import com.librarymanagement.API.entity.Patron;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PatronMapper {
    Patron toEntity(PatronDTO patronDTO);
    PatronDTO toDTO(Patron patron);
    List<PatronDTO> toDTOs(List<Patron> patrons);
}
