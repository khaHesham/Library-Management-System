package com.librarymanagement.API.service;

import com.librarymanagement.API.dto.AdminDTO;
import com.librarymanagement.API.dto.auth.AuthenticationRequest;
import com.librarymanagement.API.dto.auth.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse register(AdminDTO adminDTO);

    AuthenticationResponse authenticate(AuthenticationRequest adminDTO);

    String getUsername();
}
