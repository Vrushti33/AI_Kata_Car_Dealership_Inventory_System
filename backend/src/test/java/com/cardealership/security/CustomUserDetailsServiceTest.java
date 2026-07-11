package com.cardealership.security;

import com.cardealership.entity.Role;
import com.cardealership.entity.User;
import com.cardealership.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldLoadUserByEmail() {
        // Arrange
        User user = new User();
        user.setEmail("lightning@route66.com");
        user.setPassword("hashedpassword");
        user.setName("Lightning McQueen");
        user.setRole(Role.USER);

        when(userRepository.findByEmail("lightning@route66.com")).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername("lightning@route66.com");

        // Assert
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("lightning@route66.com");
        assertThat(userDetails.getPassword()).isEqualTo("hashedpassword");
        assertThat(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))).isTrue();
    }

    @Test
    public void shouldThrowWhenUserNotFound() {
        // Arrange
        when(userRepository.findByEmail("nonexistent@route66.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("nonexistent@route66.com");
        });
    }
}
