package org.example.issproject.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.issproject.domain.Profile;
import org.example.issproject.domain.ProfileDTO;
import org.example.issproject.domain.User;
import org.example.issproject.request.UpdateProfileRequest;
import org.example.issproject.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@CrossOrigin(origins = {"http://localhost:5173","https://uiiss.onrender.com"})
@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@Slf4j
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("")
    public ResponseEntity<ProfileDTO> getProfile(@AuthenticationPrincipal User user) {
         Profile profile = profileService.getProfile(user);
         if (profile == null) {
             return  ResponseEntity.notFound().build();
         }
         else {
             ProfileDTO profDTO = new ProfileDTO(profile.getProfilePicUrl(), profile.getDescription(), profile.getActivity(), user.getUsername());
             return ResponseEntity.ok(profDTO);
         }
    }

    @PatchMapping("")
    public ResponseEntity<Profile> updateMyProfile(
            @AuthenticationPrincipal User user,
            @RequestBody UpdateProfileRequest request) {

        if (request.getDescription() == null  && request.getActivity() == null) {
            return ResponseEntity.badRequest().build();
        }

        Profile updatedProfile = profileService.updateProfile(user, request);

        return ResponseEntity.ok(updatedProfile);
    }


    @PostMapping("/avatar")
    public ResponseEntity<String> uploadAvatar(
            @AuthenticationPrincipal User user,
            @RequestParam("file") MultipartFile file) {

        profileService.uploadImage(user.getId(), file);

        return ResponseEntity.ok("Imagine loaded succesfully");
    }
}
