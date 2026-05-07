package org.example.issproject.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.issproject.domain.Profile;
import org.example.issproject.domain.User;
import org.example.issproject.repository.ProfileRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {

    private final ProfileRepository profileRepository;

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
}
