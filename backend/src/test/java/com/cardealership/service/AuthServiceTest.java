package com.cardealership.service;

import com.cardealership.dto.AuthResponse;
import com.cardealership.dto.LoginRequest;
import com.cardealership.dto.RegisterRequest;
import com.cardealership.entity.Role;
import com.cardealership.entity.User;
import com.cardealership.exception.EmailAlreadyExistsException;
import com.cardealership.exception.InvalidCredentialsException;
import com.cardealership.repository.UserRepository;
import com.cardealership.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldRegisterNewUser() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setEmail("newuser@route66.com");
        request.setPassword("password123");
        request.setName("Cruz Ramirez");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail(request.getEmail());
        savedUser.setName(request.getName());
        savedUser.setRole(Role.USER);
        
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtUtil.generateToken(savedUser.getEmail(), "USER")).thenReturn("mockToken");

        // Act
        AuthResponse response = authService.register(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("mockToken");
        assertThat(response.getEmail()).isEqualTo(request.getEmail());
        assertThat(response.getName()).isEqualTo(request.getName());
        assertThat(response.getRole()).isEqualTo("USER");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void shouldThrowWhenEmailAlreadyExists() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setEmail("existing@route66.com");
        request.setPassword("password123");
        request.setName("Existing User");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        // Act & Assert
        assertThrows(EmailAlreadyExistsException.class, () -> {
            authService.register(request);
        });
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void shouldLoginWithValidCredentials() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("user@route66.com");
        request.setPassword("password123");

        User user = new User();
        user.setId(1L);
        user.setEmail(request.getEmail());
        user.setPassword("encodedPassword");
        user.setName("Lightning McQueen");
        user.setRole(Role.USER);

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(user.getEmail(), "USER")).thenReturn("mockToken");

        // Act
        AuthResponse response = authService.login(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("mockToken");
        assertThat(response.getEmail()).isEqualTo(request.getEmail());
        assertThat(response.getName()).isEqualTo(user.getName());
        assertThat(response.getRole()).isEqualTo("USER");
    }

    @Test
    public void shouldThrowWhenLoginWithInvalidEmail() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("wrongemail@route66.com");
        request.setPassword("password123");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> {
            authService.login(request);
        });
    }

    @Test
    public void shouldThrowWhenLoginWithWrongPassword() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("user@route66.com");
        request.setPassword("wrongpassword");

        User user = new User();
        user.setId(1L);
        user.setEmail(request.getEmail());
        user.setPassword("encodedPassword");
        user.setName("Lightning McQueen");
        user.setRole(Role.USER);

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> {
            authService.login(request);
        });
    }

    @Test
    public void shouldHashPasswordOnRegistration() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setEmail("hashme@route66.com");
        request.setPassword("password123");
        request.setName("Hasher User");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("hashedPassword");
        
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail(request.getEmail());
        savedUser.setPassword("hashedPassword");
        savedUser.setName(request.getName());
        savedUser.setRole(Role.USER);
        
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtUtil.generateToken(anyString(), anyString())).thenReturn("mockToken");

        // Act
        authService.register(request);

        // Assert
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        
        User capturedUser = userCaptor.getValue();
        assertThat(capturedUser.getPassword()).isEqualTo("hashedPassword");
        verify(passwordEncoder, times(1)).encode("password123");
    }
}
