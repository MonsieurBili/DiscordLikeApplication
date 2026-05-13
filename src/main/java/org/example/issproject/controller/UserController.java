package org.example.issproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.issproject.domain.ActivityStatus;
import org.example.issproject.domain.Profile;
import org.example.issproject.domain.User;
import org.example.issproject.request.LoginRequest;
import org.example.issproject.request.RegisterRequest;
import org.example.issproject.service.ProfileService;
import org.example.issproject.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final ProfileService profileService;

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

        Profile profile = new Profile();
        profile.setActivity(ActivityStatus.ONLINE);
        profile.setUser(newUser);
        newUser.setProfile(profile);
        User savedUser= null;
        try {
            savedUser = userService.register(newUser);
            profileService.save(profile);
            log.info("Register completed for username={} id={}", request.getUsername(), savedUser.getId());
            return ResponseEntity.ok("Successful register");
        } catch (Exception e) {
            log.info("Register failed because e=",e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Register failed");
        }

    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        String token = userService.login(request.getUsername(), request.getPassword());

        User user = userService.findByUsername(request.getUsername());
        if (user != null) {
            Profile profile = profileService.getProfile(user);
            profile.setActivity(ActivityStatus.ONLINE);
            profileService.save(profile);
            log.info("User {} set to ONLINE on login", user.getUsername());
        } else {
            log.warn("Login succeeded but user not found for username={}", request.getUsername());
        }

        return ResponseEntity.ok(token);
    }

}
