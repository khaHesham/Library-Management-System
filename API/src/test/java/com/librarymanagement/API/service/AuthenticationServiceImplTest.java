
package com.librarymanagement.API.service;

import com.librarymanagement.API.dto.AdminDTO;
import com.librarymanagement.API.dto.auth.AuthenticationRequest;
import com.librarymanagement.API.dto.auth.AuthenticationResponse;
import com.librarymanagement.API.entity.Admin;
import com.librarymanagement.API.exception.admin.AdminAlreadyExistsException;
import com.librarymanagement.API.exception.admin.AdminNotFoundException;
import com.librarymanagement.API.repository.AdminRepository;
import com.librarymanagement.API.service.JwtService;
import com.librarymanagement.API.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AuthenticationServiceImplTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private AdminDTO adminDTO;
    private AuthenticationRequest authRequest;
    private Admin admin;
    private String jwtToken;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        adminDTO = new AdminDTO("testuser", "testpass");
        authRequest = new AuthenticationRequest();
        authRequest.setUsername("testuser");
        authRequest.setPassword("testpass");
        admin = new Admin();
        admin.setUsername("testuser");
        admin.setPassword("encodedpass");
        jwtToken = "dummy.jwt.token";

        when(passwordEncoder.encode(anyString())).thenReturn("encodedpass");
        when(jwtService.generateToken(any(Admin.class))).thenReturn(jwtToken);
    }

    @Test
    void register_ShouldReturnAuthenticationResponse_WhenAdminDoesNotExist() {
        when(adminRepository.findAdminByUsername(anyString())).thenReturn(Optional.empty());
        when(adminRepository.save(any(Admin.class))).thenReturn(admin);

        AuthenticationResponse response = authenticationService.register(adminDTO);

        assertNotNull(response);
        assertEquals(jwtToken, response.getToken());
        verify(adminRepository, times(1)).findAdminByUsername(adminDTO.getUsername());
        verify(adminRepository, times(1)).save(any(Admin.class));
        verify(jwtService, times(1)).generateToken(any(Admin.class));
    }

    @Test
    void register_ShouldThrowAdminAlreadyExistsException_WhenAdminExists() {
        when(adminRepository.findAdminByUsername(anyString())).thenReturn(Optional.of(admin));

        assertThrows(AdminAlreadyExistsException.class, () -> authenticationService.register(adminDTO));

        verify(adminRepository, times(1)).findAdminByUsername(adminDTO.getUsername());
        verify(adminRepository, never()).save(any(Admin.class));
        verify(jwtService, never()).generateToken(any(Admin.class));
    }

    @Test
    void authenticate_ShouldReturnAuthenticationResponse_WhenCredentialsAreValid() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(adminRepository.findAdminByUsername(anyString())).thenReturn(Optional.of(admin));

        AuthenticationResponse response = authenticationService.authenticate(authRequest);

        assertNotNull(response);
        assertEquals(jwtToken, response.getToken());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(adminRepository, times(1)).findAdminByUsername(authRequest.getUsername());
        verify(jwtService, times(1)).generateToken(any(Admin.class));
    }

    @Test
    void authenticate_ShouldThrowAdminNotFoundException_WhenAdminDoesNotExist() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(adminRepository.findAdminByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(AdminNotFoundException.class, () -> authenticationService.authenticate(authRequest));

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(adminRepository, times(1)).findAdminByUsername(authRequest.getUsername());
        verify(jwtService, never()).generateToken(any(Admin.class));
    }

    @Test
    void getUsername_ShouldReturnUsername_WhenTokenIsValid() {
        when(jwtService.extractUsername(anyString())).thenReturn("testuser");

        String username = authenticationService.getUsername();

        assertEquals("testuser", username);
        verify(jwtService, times(1)).extractUsername(jwtToken);
    }
}
