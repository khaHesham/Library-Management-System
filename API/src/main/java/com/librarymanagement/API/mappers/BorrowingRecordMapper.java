package com.librarymanagement.API.mappers;

import com.librarymanagement.API.dto.BorrowingRecordDTO;
import com.librarymanagement.API.entity.BorrowingRecord;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BorrowingRecordMapper {
    BorrowingRecord toEntity(BorrowingRecordDTO borrowingRecordDTO);
     BorrowingRecordDTO toDTO(BorrowingRecord borrowingRecord);
}
