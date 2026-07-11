package com.cardealership.repository;

import com.cardealership.entity.Role;
import com.cardealership.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldSaveAndFindUserByEmail() {
        // Arrange
        User user = new User();
        user.setEmail("mcqueen@route66.com");
        user.setPassword("password123");
        user.setName("Lightning McQueen");
        user.setRole(Role.USER);

        // Act
        User savedUser = userRepository.save(user);
        Optional<User> foundUser = userRepository.findByEmail("mcqueen@route66.com");

        // Assert
        assertThat(savedUser.getId()).isNotNull();
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("Lightning McQueen");
    }

    @Test
    public void shouldReturnEmptyWhenEmailNotFound() {
        // Act
        Optional<User> foundUser = userRepository.findByEmail("nonexistent@route66.com");

        // Assert
        assertThat(foundUser).isEmpty();
    }

    @Test
    public void shouldNotAllowDuplicateEmails() {
        // Arrange
        User user1 = new User();
        user1.setEmail("duplicate@route66.com");
        user1.setPassword("password123");
        user1.setName("User One");
        user1.setRole(Role.USER);
        userRepository.saveAndFlush(user1);

        User user2 = new User();
        user2.setEmail("duplicate@route66.com");
        user2.setPassword("password456");
        user2.setName("User Two");
        user2.setRole(Role.USER);

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.saveAndFlush(user2);
        });
    }

    @Test
    public void shouldCheckIfEmailExists() {
        // Arrange
        User user = new User();
        user.setEmail("exists@route66.com");
        user.setPassword("password123");
        user.setName("Existing User");
        user.setRole(Role.USER);
        userRepository.save(user);

        // Act & Assert
        assertThat(userRepository.existsByEmail("exists@route66.com")).isTrue();
        assertThat(userRepository.existsByEmail("nonexistent@route66.com")).isFalse();
    }
}
