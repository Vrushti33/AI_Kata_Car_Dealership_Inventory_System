package com.cardealership.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JwtUtilTest {

    private JwtUtil jwtUtil;
    private final String testSecret = "dGVzdC1zZWNyZXQta2V5LXZvci11bml0LXRlc3Rpbmctb25seS1tdXN0LWJlLTI1Ni1iaXRz"; // 256+ bit key
    private final long testExpiration = 3600000; // 1 hour

    @BeforeEach
    public void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", testSecret);
        ReflectionTestUtils.setField(jwtUtil, "expirationMs", testExpiration);
        jwtUtil.init();
    }

    @Test
    public void shouldGenerateValidToken() {
        String token = jwtUtil.generateToken("lightning@route66.com", "USER");
        assertThat(token).isNotBlank();
    }

    @Test
    public void shouldExtractUsernameFromToken() {
        String email = "lightning@route66.com";
        String token = jwtUtil.generateToken(email, "USER");
        
        String extractedEmail = jwtUtil.extractUsername(token);
        assertThat(extractedEmail).isEqualTo(email);
    }

    @Test
    public void shouldExtractRoleFromToken() {
        String token = jwtUtil.generateToken("lightning@route66.com", "USER");
        
        String extractedRole = jwtUtil.extractRole(token);
        assertThat(extractedRole).isEqualTo("USER");
    }

    @Test
    public void shouldReturnTrueForValidToken() {
        String email = "lightning@route66.com";
        String token = jwtUtil.generateToken(email, "USER");
        
        boolean isValid = jwtUtil.validateToken(token, email);
        assertThat(isValid).isTrue();
    }

    @Test
    public void shouldReturnFalseForExpiredToken() {
        // Set short expiration
        ReflectionTestUtils.setField(jwtUtil, "expirationMs", -1000L); // already expired 1 sec ago
        jwtUtil.init();
        
        String token = jwtUtil.generateToken("lightning@route66.com", "USER");
        
        // Validation should fail due to expiry
        boolean isValid = jwtUtil.validateToken(token, "lightning@route66.com");
        assertThat(isValid).isFalse();
    }

    @Test
    public void shouldReturnFalseForTamperedToken() {
        String token = jwtUtil.generateToken("lightning@route66.com", "USER");
        String tamperedToken = token + "tamperedExtraBytes";
        
        boolean isValid = jwtUtil.validateToken(tamperedToken, "lightning@route66.com");
        assertThat(isValid).isFalse();
    }
}
