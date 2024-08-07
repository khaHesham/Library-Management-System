package com.librarymanagement.API.mappers;


import com.librarymanagement.API.dto.BookDTO;
import com.librarymanagement.API.entity.Book;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {
    Book toEntity(BookDTO bookDTO);
    BookDTO toDTO(Book book);
    List<BookDTO> toDTOs(List<Book> books);
}
