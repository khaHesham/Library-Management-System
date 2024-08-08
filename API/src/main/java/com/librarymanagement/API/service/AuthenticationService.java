package com.librarymanagement.API.service;

import com.librarymanagement.API.dto.AdminDTO;
import com.librarymanagement.API.dto.auth.AuthenticationResponse;

public interface AuthenticationService {
    /**
     * Registers a new admin.
     * @param adminDTO the DTO containing admin details
     * @return AuthenticationResponse with the JWT token
     */
    AuthenticationResponse register(AdminDTO adminDTO);

    /**
     * Authenticates an admin and returns a JWT token.
     * @param adminDTO the DTO containing admin credentials
     * @return AuthenticationResponse with the JWT token
     */
    AuthenticationResponse authenticate(AdminDTO adminDTO);

    /**
     * Retrieves the username from the JWT token.
     * @return the username of the authenticated admin
     */
    String getUsername();
}
