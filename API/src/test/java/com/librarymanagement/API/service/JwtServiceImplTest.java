package com.librarymanagement.API.service;

import com.librarymanagement.API.service.impl.JwtServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtServiceImplTest {

    @InjectMocks
    private JwtServiceImpl jwtService;

    private UserDetails userDetails;
    private static final String SECRET_KEY = "0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF"; // 256-bit key

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userDetails = User.withUsername("testuser")
                .password("testpass")
                .roles("USER") // Role is not used in the service, but needed for UserDetails
                .build();
    }

    @Test
    void testGenerateToken() {
        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
        assertTrue(token.startsWith("eyJ"));
    }

    @Test
    void testGenerateTokenWithExtraClaims() {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("additionalClaim", "value");
        String token = jwtService.generateToken(extraClaims, userDetails);

        assertNotNull(token);
        assertTrue(token.startsWith("eyJ"));
    }

    @Test
    void testExtractUsername() {
        String token = jwtService.generateToken(userDetails);
        String username = jwtService.extractUsername(token);

        assertEquals(userDetails.getUsername(), username);
    }

    @Test
    void testValidateToken() {
        String token = jwtService.generateToken(userDetails);
        boolean isValid = jwtService.validateToken(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    void testValidateTokenWithInvalidToken() {
        String token = jwtService.generateToken(userDetails);
        String invalidToken = token + "extra";
        boolean isValid = jwtService.validateToken(invalidToken, userDetails);

        assertFalse(isValid);
    }

    @Test
    void testValidateTokenWithExpiredToken() {
        // Override expiration to a past date
        JwtServiceImpl spyService = spy(jwtService);
        doReturn(new Date(System.currentTimeMillis() - 10000)).when(spyService).extractExpiration(anyString());

        String token = spyService.generateToken(userDetails);
        boolean isValid = spyService.validateToken(token, userDetails);

        assertFalse(isValid);
    }

    @Test
    void testExtractAllClaims() {
        String token = jwtService.generateToken(userDetails);
        Claims claims = jwtService.extractAllClaims(token);

        assertNotNull(claims);
        assertEquals(userDetails.getUsername(), claims.getSubject());
    }

    @Test
    void testExtractClaim() {
        String token = jwtService.generateToken(userDetails);
        String username = jwtService.extractClaim(token, Claims::getSubject);

        assertEquals(userDetails.getUsername(), username);
    }

    @Test
    void testGetSigningKey() {
        Key signingKey = jwtService.getSigningKey();

        assertNotNull(signingKey);
        assertEquals(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), signingKey);
    }
}