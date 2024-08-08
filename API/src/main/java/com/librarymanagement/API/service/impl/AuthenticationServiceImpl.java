package com.librarymanagement.API.service.impl;

import com.librarymanagement.API.dto.AdminDTO;
import com.librarymanagement.API.dto.auth.AuthenticationResponse;
import com.librarymanagement.API.exception.admin.AdminAlreadyExistsException;
import com.librarymanagement.API.exception.admin.AdminNotFoundException;
import com.librarymanagement.API.repository.AdminRepository;
import com.librarymanagement.API.entity.Admin;
import com.librarymanagement.API.service.AuthenticationService;
import com.librarymanagement.API.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private String jwtToken;

    @Override
    public AuthenticationResponse register(AdminDTO adminDTO) {

        if (adminRepository.findAdminByUsername(adminDTO.getUsername()).isPresent()) {
            throw new AdminAlreadyExistsException("Admin already exists");
        }

        var admin = new Admin();
        admin.setUsername(adminDTO.getUsername());
        admin.setPassword(passwordEncoder.encode(adminDTO.getPassword()));
        adminRepository.save(admin);

        jwtToken = jwtService.generateToken(admin);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AdminDTO adminDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        adminDTO.getUsername(),
                        adminDTO.getPassword()
                )
        );

        Admin admin = adminRepository.findAdminByUsername(adminDTO.getUsername())
                .orElseThrow(() -> new AdminNotFoundException("Admin not found"));

        jwtToken = jwtService.generateToken(admin);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public String getUsername() {
        return jwtService.extractUsername(jwtToken);
    }
}
