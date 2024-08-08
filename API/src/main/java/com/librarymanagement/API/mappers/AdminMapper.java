package com.librarymanagement.API.mappers;

import com.librarymanagement.API.dto.AdminDTO;
import com.librarymanagement.API.entity.Admin;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdminMapper {
    Admin toEntity(AdminDTO bookDTO);
    AdminDTO toDTO(Admin book);
}
