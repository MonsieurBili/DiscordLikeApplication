package org.example.issproject.service;

import org.example.issproject.domain.User;
import org.example.issproject.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void registerSavesNewUserAndEncodesPassword() {
        User user = new User();
        user.setUsername("ana");
        user.setEmail("ana@example.com");
        user.setPasswordHash("secret123");

        when(userRepository.findByUsername("ana")).thenReturn(null);
        when(userRepository.findByEmail("ana@example.com")).thenReturn(null);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User savedUser = userService.register(user);

        verify(userRepository).save(user);
        assertEquals("ana", savedUser.getUsername());
        new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().matches("secret123", savedUser.getPasswordHash());
    }

    @Test
    void registerRejectsExistingUsername() {
        User user = new User();
        user.setUsername("ana");
        user.setEmail("ana@example.com");
        user.setPasswordHash("secret123");

        when(userRepository.findByUsername("ana")).thenReturn(new User());

        assertThrows(IllegalArgumentException.class, () -> userService.register(user));
        verify(userRepository, never()).save(any());
    }
}
