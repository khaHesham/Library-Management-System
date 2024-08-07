package com.librarymanagement.API.service;

import com.librarymanagement.API.dto.PatronDTO;

import java.util.List;


public interface PatronService {
    List<PatronDTO> getAllPatrons();
    PatronDTO getPatronById(Long id);
    PatronDTO addPatron(PatronDTO patronDto);
    PatronDTO updatePatron(Long id, PatronDTO patronDto);
    boolean deletePatron(Long id);
}
