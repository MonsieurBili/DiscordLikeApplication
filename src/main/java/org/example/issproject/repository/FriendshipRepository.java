package org.example.issproject.repository;

import org.example.issproject.domain.Friendship;
import org.example.issproject.domain.FriendshipStatus;
import org.example.issproject.domain.Profile;
import org.example.issproject.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface FriendshipRepository extends JpaRepository<Friendship, UUID> {
    @Query("SELECT f FROM Friendship f WHERE (f.user1 = :user OR f.user2 = :user) AND f.status = :status")
    List<Friendship> findByUserAndStatus(@Param("user") User user, @Param("status") FriendshipStatus status);
    @Query("SELECT COUNT(f) > 0 FROM Friendship f WHERE (f.user1 = :user1 AND f.user2 = :user2) OR (f.user1 = :user2 AND f.user2 = :user1)")
    boolean friendshipExistsBetween(@Param("user1") User user1, @Param("user2") User user2);
}
