package org.example.issproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.issproject.domain.User;
import org.example.issproject.request.LoginRequest;
import org.example.issproject.request.RegisterRequest;
import org.example.issproject.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        log.info("Register request username={} email={}", request.getUsername(), request.getEmail());

        if (request.getUsername() == null || request.getUsername().isEmpty()
                || request.getEmail() == null || request.getEmail().isEmpty()
                || request.getPassword() == null || request.getPassword().isEmpty()
                || request.getPassword().length() < 6) {
            return ResponseEntity.badRequest().body("Invalid registration payload");
        }

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPasswordHash(request.getPassword());

        User savedUser = userService.register(newUser);
        log.info("Register completed for username={} id={}", request.getUsername(), savedUser.getId());

        return ResponseEntity.ok("Successful register");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        String token = userService.login(request.getUsername(), request.getPassword());

        return ResponseEntity.ok(token);
    }

}
