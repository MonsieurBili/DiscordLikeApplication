package org.example.issproject.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.issproject.domain.User;
import org.example.issproject.repository.UserRepository;
import org.example.issproject.security.JWTService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public User register(User user) {
        log.info("Register User method called for username={}", user.getUsername());

        if (userRepository.findByUsername(user.getUsername()) != null) {
            log.warn("Registration rejected because username already exists: {}", user.getUsername());
            throw new IllegalArgumentException("Username already exists");
        }

        if (userRepository.findByEmail(user.getEmail()) != null) {
            log.warn("Registration rejected because email already exists: {}", user.getEmail());
            throw new IllegalArgumentException("Email already exists");
        }

        String parolaCriptata = passwordEncoder.encode(user.getPasswordHash());
        user.setPasswordHash(parolaCriptata);
        User savedUser = userRepository.save(user);
        log.info("User saved successfully: {}", savedUser.getUsername());
        return savedUser;
    }

    public String login(String username, String password) {
        log.info("Login method called for username={}", username);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        if (authentication.isAuthenticated()) {
            log.info("Authentication successful for username={}", username);
            return jwtService.generateToken(username);
        } else {
            throw new RuntimeException("Failed to authenticate user");
        }
    }
}
