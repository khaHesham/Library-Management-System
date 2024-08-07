package com.librarymanagement.API.controller;

import com.librarymanagement.API.dto.PatronDTO;
import com.librarymanagement.API.exception.GlobalExceptionHandler;
import com.librarymanagement.API.exception.patron.PatronNotFoundException;
import com.librarymanagement.API.service.PatronService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patrons")
public class PatronController extends GlobalExceptionHandler {
    @Autowired
    private PatronService patronService;

    @GetMapping
    public ResponseEntity<List<PatronDTO>> getAllPatrons() {
        List<PatronDTO> patrons = patronService.getAllPatrons();
        return new ResponseEntity<>(patrons, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatronDTO> getPatronById(@PathVariable Long id) {
        PatronDTO patron = patronService.getPatronById(id);
        if (patron == null) {
            throw new PatronNotFoundException("Patron not found with id: " + id);
        }
        return new ResponseEntity<>(patron, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PatronDTO> addPatron(@Valid @RequestBody PatronDTO patronDto) {
        PatronDTO savedPatron = patronService.addPatron(patronDto);
        return new ResponseEntity<>(savedPatron, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatronDTO> updatePatron(@PathVariable Long id, @Valid @RequestBody PatronDTO patronDto) {
        PatronDTO updatedPatron = patronService.updatePatron(id, patronDto);
        if (updatedPatron == null) {
            throw new PatronNotFoundException("Cannot update, patron not found with id: " + id);
        }
        return new ResponseEntity<>(updatedPatron, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatron(@PathVariable Long id) {
        boolean deleted = patronService.deletePatron(id);
        if (!deleted) {
            throw new PatronNotFoundException("Cannot delete, patron not found with id: " + id);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
