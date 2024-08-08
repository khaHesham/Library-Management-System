package com.librarymanagement.API.controller;

import com.librarymanagement.API.dto.PatronDTO;
import com.librarymanagement.API.exception.patron.PatronNotFoundException;
import com.librarymanagement.API.service.PatronService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PatronControllerTest {

    @Mock
    private PatronService patronService;

    @InjectMocks
    private PatronController patronController;

    private PatronDTO patronDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        patronDTO = new PatronDTO(
                "John Doe",
                "johndoe@example.com",
                "+1234567890",
                "123 Main St",
                "Springfield",
                "IL",
                "62704"
        );
    }

    @Test
    void getAllPatrons_ShouldReturnOkStatusWithListOfPatrons() {
        List<PatronDTO> patrons = new ArrayList<>();
        patrons.add(patronDTO);

        when(patronService.getAllPatrons()).thenReturn(patrons);

        ResponseEntity<List<PatronDTO>> response = patronController.getAllPatrons();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(patronService, times(1)).getAllPatrons();
    }

    @Test
    void getPatronById_ShouldReturnOkStatus_WhenPatronExists() {
        when(patronService.getPatronById(anyLong())).thenReturn(patronDTO);

        ResponseEntity<PatronDTO> response = patronController.getPatronById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John Doe", response.getBody().getName());
        verify(patronService, times(1)).getPatronById(1L);
    }

    @Test
    void getPatronById_ShouldThrowPatronNotFoundException_WhenPatronDoesNotExist() {
        when(patronService.getPatronById(anyLong())).thenReturn(null);

        assertThrows(PatronNotFoundException.class, () -> patronController.getPatronById(1L));

        verify(patronService, times(1)).getPatronById(1L);
    }

    @Test
    void addPatron_ShouldReturnCreatedStatus_WhenPatronIsAddedSuccessfully() {
        when(patronService.addPatron(any(PatronDTO.class))).thenReturn(patronDTO);

        ResponseEntity<PatronDTO> response = patronController.addPatron(patronDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John Doe", response.getBody().getName());
        verify(patronService, times(1)).addPatron(any(PatronDTO.class));
    }

    @Test
    void updatePatron_ShouldReturnOkStatus_WhenPatronIsUpdatedSuccessfully() {
        when(patronService.updatePatron(anyLong(), any(PatronDTO.class))).thenReturn(patronDTO);

        ResponseEntity<PatronDTO> response = patronController.updatePatron(1L, patronDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John Doe", response.getBody().getName());
        verify(patronService, times(1)).updatePatron(anyLong(), any(PatronDTO.class));
    }

    @Test
    void updatePatron_ShouldThrowPatronNotFoundException_WhenPatronDoesNotExist() {
        when(patronService.updatePatron(anyLong(), any(PatronDTO.class))).thenReturn(null);

        assertThrows(PatronNotFoundException.class, () -> patronController.updatePatron(1L, patronDTO));

        verify(patronService, times(1)).updatePatron(anyLong(), any(PatronDTO.class));
    }

    @Test
    void deletePatron_ShouldReturnNoContentStatus_WhenPatronIsDeletedSuccessfully() {
        when(patronService.deletePatron(anyLong())).thenReturn(true);

        ResponseEntity<Void> response = patronController.deletePatron(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(patronService, times(1)).deletePatron(1L);
    }

    @Test
    void deletePatron_ShouldThrowPatronNotFoundException_WhenPatronDoesNotExist() {
        when(patronService.deletePatron(anyLong())).thenReturn(false);

        assertThrows(PatronNotFoundException.class, () -> patronController.deletePatron(1L));

        verify(patronService, times(1)).deletePatron(1L);
    }
}
