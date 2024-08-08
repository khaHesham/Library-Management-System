package com.librarymanagement.API.controller;

import com.librarymanagement.API.dto.AdminDTO;
import com.librarymanagement.API.dto.auth.AuthenticationRequest;
import com.librarymanagement.API.dto.auth.AuthenticationResponse;
import com.librarymanagement.API.exception.admin.AdminAlreadyExistsException;
import com.librarymanagement.API.exception.admin.AdminNotFoundException;
import com.librarymanagement.API.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    private AdminDTO adminDTO;
    private AuthenticationRequest authRequest;
    private AuthenticationResponse authResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        adminDTO = new AdminDTO("testuser", "testpass");
        authRequest = new AuthenticationRequest();
        authRequest.setUsername("testuser");
        authRequest.setPassword("testpass");
        authResponse = AuthenticationResponse.builder()
                .token("dummy.jwt.token")
                .build();
    }

    @Test
    void register_ShouldReturnOk_WhenRegistrationIsSuccessful() {
        when(authenticationService.register(any(AdminDTO.class))).thenReturn(authResponse);

        ResponseEntity<AuthenticationResponse> response = authenticationController.register(adminDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authResponse, response.getBody());
        verify(authenticationService, times(1)).register(adminDTO);
    }

    @Test
    void register_ShouldThrowAdminAlreadyExistsException_WhenAdminExists() {
        when(authenticationService.register(any(AdminDTO.class))).thenThrow(new AdminAlreadyExistsException("Admin already exists"));

        assertThrows(AdminAlreadyExistsException.class, () -> authenticationController.register(adminDTO));

        verify(authenticationService, times(1)).register(adminDTO);
    }

    @Test
    void authenticate_ShouldReturnOk_WhenAuthenticationIsSuccessful() {
        when(authenticationService.authenticate(any(AuthenticationRequest.class))).thenReturn(authResponse);

        ResponseEntity<AuthenticationResponse> response = authenticationController.authenticate(authRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authResponse, response.getBody());
        verify(authenticationService, times(1)).authenticate(authRequest);
    }

    @Test
    void authenticate_ShouldReturnUnauthorized_WhenAuthenticationFails() {
        when(authenticationService.authenticate(any(AuthenticationRequest.class))).thenThrow(new AdminNotFoundException("Admin not found"));

        ResponseEntity<AuthenticationResponse> response = authenticationController.authenticate(authRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
        verify(authenticationService, times(1)).authenticate(authRequest);
    }
}
