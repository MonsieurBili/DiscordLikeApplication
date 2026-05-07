package org.example.issproject.repository;

import org.example.issproject.domain.Profile;
import org.example.issproject.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProfileRepository extends JpaRepository<Profile, UUID> {

    Profile findProfileByUser(User user);
}
