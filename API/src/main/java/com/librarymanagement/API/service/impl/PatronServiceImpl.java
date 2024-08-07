package com.librarymanagement.API.service.impl;

import com.librarymanagement.API.dto.PatronDTO;
import com.librarymanagement.API.entity.Patron;
import com.librarymanagement.API.mappers.PatronMapper;
import com.librarymanagement.API.repository.PatronRepository;
import com.librarymanagement.API.service.PatronService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatronServiceImpl implements PatronService {
    @Autowired
    private PatronRepository patronRepository;

    @Autowired
    private PatronMapper patronMapper;

    @Override
    public List<PatronDTO> getAllPatrons() {
        List<Patron> patrons = patronRepository.findAll();
        return patronMapper.toDTOs(patrons);
    }

    @Override
    public PatronDTO getPatronById(Long id) {
        Patron patron = patronRepository.findById(id).orElse(null);
        return patronMapper.toDTO(patron);
    }

    @Override
    public PatronDTO addPatron(PatronDTO patronDto) {
        Patron newPatron = patronRepository.save(patronMapper.toEntity(patronDto));
        return patronMapper.toDTO(newPatron);
    }

    @Override
    public PatronDTO updatePatron(Long id, PatronDTO patronDto) {
        if(!patronRepository.existsById(id)){
            return null;
        }
        Patron updatedPatron = patronRepository.save(patronMapper.toEntity(patronDto));
        return patronMapper.toDTO(updatedPatron);
    }

    @Override
    public boolean deletePatron(Long id) {
        if(!patronRepository.existsById(id)){
            return false;
        }
        patronRepository.deleteById(id);
        return true;
    }
}
