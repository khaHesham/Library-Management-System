package com.librarymanagement.API.controller;

import com.librarymanagement.API.dto.AdminDTO;
import com.librarymanagement.API.dto.auth.AuthenticationRequest;
import com.librarymanagement.API.dto.auth.AuthenticationResponse;
import com.librarymanagement.API.exception.GlobalExceptionHandler;
import com.librarymanagement.API.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController extends GlobalExceptionHandler {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
           @Valid @RequestBody AdminDTO request
    ) {
        AuthenticationResponse response = authenticationService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
           @Valid @RequestBody AuthenticationRequest request
    ) {
        try {
            AuthenticationResponse response = authenticationService.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Handle specific exceptions or log the error
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}
