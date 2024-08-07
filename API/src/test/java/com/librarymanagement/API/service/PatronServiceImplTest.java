package com.librarymanagement.API.service;

import com.librarymanagement.API.dto.PatronDTO;
import com.librarymanagement.API.entity.Patron;
import com.librarymanagement.API.mappers.PatronMapper;
import com.librarymanagement.API.repository.PatronRepository;
import com.librarymanagement.API.service.impl.PatronServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Validated
class PatronServiceImplTest {

    @Mock
    private PatronRepository patronRepository;

    @Mock
    private PatronMapper patronMapper;

    @InjectMocks
    private PatronServiceImpl patronService;

    private Patron patron;
    private PatronDTO patronDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        patron = new Patron();
        patron.setId(1L);
        patron.setName("Test Patron");
        patron.setEmail("test@example.com");
        patron.setPhoneNumber("+1234567890");
        patron.setAddress("123 Main St");
        patron.setCity("Test City");
        patron.setState("Test State");
        patron.setZipCode("12345");

        patronDTO = new PatronDTO(
                "Test Patron",
                "test@example.com",
                "+1234567890",
                "123 Main St",
                "Test City",
                "Test State",
                "12345"
        );
    }

    @Test
    void getAllPatrons_ShouldReturnListOfPatronDTOs() {
        when(patronRepository.findAll()).thenReturn(List.of(patron));
        when(patronMapper.toDTOs(anyList())).thenReturn(List.of(patronDTO));

        List<PatronDTO> result = patronService.getAllPatrons();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(patronDTO.getName(), result.get(0).getName());
        verify(patronRepository, times(1)).findAll();
        verify(patronMapper, times(1)).toDTOs(anyList());
    }

    @Test
    void getPatronById_ShouldReturnPatronDTO_WhenPatronExists() {
        when(patronRepository.findById(anyLong())).thenReturn(Optional.of(patron));
        when(patronMapper.toDTO(any(Patron.class))).thenReturn(patronDTO);

        PatronDTO result = patronService.getPatronById(1L);

        assertNotNull(result);
        assertEquals(patronDTO.getName(), result.getName());
        verify(patronRepository, times(1)).findById(1L);
        verify(patronMapper, times(1)).toDTO(any(Patron.class));
    }

    @Test
    void getPatronById_ShouldReturnNull_WhenPatronDoesNotExist() {
        when(patronRepository.findById(anyLong())).thenReturn(Optional.empty());

        PatronDTO result = patronService.getPatronById(1L);

        assertNull(result);
        verify(patronRepository, times(1)).findById(1L);
        verify(patronMapper, never()).toDTO(any(Patron.class));
    }

    @Test
    void addPatron_ShouldReturnPatronDTO_WhenPatronIsSaved() {
        when(patronRepository.save(any(Patron.class))).thenReturn(patron);
        when(patronMapper.toEntity(any(PatronDTO.class))).thenReturn(patron);
        when(patronMapper.toDTO(any(Patron.class))).thenReturn(patronDTO);

        PatronDTO result = patronService.addPatron(patronDTO);

        assertNotNull(result);
        assertEquals(patronDTO.getName(), result.getName());
        verify(patronRepository, times(1)).save(any(Patron.class));
        verify(patronMapper, times(1)).toEntity(any(PatronDTO.class));
        verify(patronMapper, times(1)).toDTO(any(Patron.class));
    }

    @Test
    void addPatron_ShouldThrowException_WhenPatronDTOIsInvalid() {
        PatronDTO invalidPatronDTO = new PatronDTO(
                "T", // Invalid name (too short)
                "invalid-email", // Invalid email format
                "123", // Invalid phone number
                "", // Blank address
                "", // Blank city
                "", // Blank state
                "12" // Invalid zip code
        );

        assertThrows(ConstraintViolationException.class, () -> {
            patronService.addPatron(invalidPatronDTO);
        });
        verify(patronRepository, never()).save(any(Patron.class));
    }

    @Test
    void updatePatron_ShouldReturnPatronDTO_WhenPatronIsUpdated() {
        when(patronRepository.existsById(anyLong())).thenReturn(true);
        when(patronRepository.save(any(Patron.class))).thenReturn(patron);
        when(patronMapper.toEntity(any(PatronDTO.class))).thenReturn(patron);
        when(patronMapper.toDTO(any(Patron.class))).thenReturn(patronDTO);

        PatronDTO result = patronService.updatePatron(1L, patronDTO);

        assertNotNull(result);
        assertEquals(patronDTO.getName(), result.getName());
        verify(patronRepository, times(1)).existsById(1L);
        verify(patronRepository, times(1)).save(any(Patron.class));
        verify(patronMapper, times(1)).toEntity(any(PatronDTO.class));
        verify(patronMapper, times(1)).toDTO(any(Patron.class));
    }

    @Test
    void updatePatron_ShouldThrowException_WhenPatronDTOIsInvalid() {
        PatronDTO invalidPatronDTO = new PatronDTO(
                "T", // Invalid name
                "invalid-email", // Invalid email
                "123", // Invalid phone number
                "", // Blank address
                "", // Blank city
                "", // Blank state
                "12" // Invalid zip code
        );

        when(patronRepository.existsById(anyLong())).thenReturn(true);

        assertThrows(ConstraintViolationException.class, () -> {
            patronService.updatePatron(1L, invalidPatronDTO);
        });
        verify(patronRepository, never()).save(any(Patron.class));
    }

    @Test
    void updatePatron_ShouldReturnNull_WhenPatronDoesNotExist() {
        when(patronRepository.existsById(anyLong())).thenReturn(false);

        PatronDTO result = patronService.updatePatron(1L, patronDTO);

        assertNull(result);
        verify(patronRepository, times(1)).existsById(1L);
        verify(patronRepository, never()).save(any(Patron.class));
        verify(patronMapper, never()).toEntity(any(PatronDTO.class));
        verify(patronMapper, never()).toDTO(any(Patron.class));
    }

    @Test
    void deletePatron_ShouldReturnTrue_WhenPatronIsDeleted() {
        when(patronRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(patronRepository).deleteById(anyLong());

        boolean result = patronService.deletePatron(1L);

        assertTrue(result);
        verify(patronRepository, times(1)).existsById(1L);
        verify(patronRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletePatron_ShouldReturnFalse_WhenPatronDoesNotExist() {
        when(patronRepository.existsById(anyLong())).thenReturn(false);

        boolean result = patronService.deletePatron(1L);

        assertFalse(result);
        verify(patronRepository, times(1)).existsById(1L);
        verify(patronRepository, never()).deleteById(anyLong());
    }
}