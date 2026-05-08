package org.example.issproject.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.issproject.domain.Profile;
import org.example.issproject.domain.User;
import org.example.issproject.image.CloudinaryResponse;
import org.example.issproject.image.CloudinaryService;
import org.example.issproject.image.FileUploadUtil;
import org.example.issproject.repository.ProfileRepository;
import org.example.issproject.request.UpdateProfileRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {

    private final ProfileRepository profileRepository;

    private final CloudinaryService cloudinaryService;

    public Profile getProfile(User user) throws UsernameNotFoundException {
        Profile profile = profileRepository.findProfileByUser(user);
        if (profile == null) {
            log.info("Profile not found");
            throw  new UsernameNotFoundException("User not found");
        }
        else {
            log.info("Profile found for User: " + user.getUsername());
            return profile;
        }
    }

    @Transactional
    public void uploadImage(final UUID userUuid, final MultipartFile file) {
        log.info("Starting image upload");
        FileUploadUtil.assertAllowedExtension(file, FileUploadUtil.IMAGE_PATTERN);
        String fileName = FileUploadUtil.getFileName(userUuid.toString());

        CloudinaryResponse response = cloudinaryService.uploadFile(file, fileName);

        Profile profile = profileRepository.findById(userUuid)
                .orElseThrow(() -> new RuntimeException("Profile not found " + userUuid));

        profile.setProfilePicUrl(response.getSecureUrl());
        profileRepository.save(profile);

        log.info("Succesfull upload of profile pic for user: {}", userUuid);
    }



    @Transactional
    public Profile updateProfile(User user, UpdateProfileRequest request) {
        log.info("Profile updating profile");
        Profile profile = getProfile(user);
        if (request.getDescription() != null) {
            profile.setDescription(request.getDescription());
            log.info("Description updated");
        }
        if (request.getActivity() != null) {
            profile.setActivity(request.getActivity());
            log.info("Activity updated");
        }
        log.info("Succesfull update of profile");
        return profileRepository.save(profile);
    }

    @Transactional
    public Profile save(Profile profile)
    {
        log.info("Saving profile {}", profile);
        profileRepository.save(profile);
        return profile;
    }
}
