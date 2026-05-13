package org.example.issproject.repository;

import org.example.issproject.domain.Conversation;
import org.example.issproject.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ConversationRepository extends JpaRepository<Conversation, UUID> {

    Conversation findConversationByUser1AndUser2(User user1, User user2);

    @Query("SELECT c FROM Conversation c WHERE c.user1 = :user OR c.user2 = :user ORDER BY c.lastMessageTime DESC")
    List<Conversation> findByUserOrderByLastMessageTimeDesc(@Param("user") User user);

    @Query("SELECT c FROM Conversation c WHERE (c.user1 = :u1 AND c.user2 = :u2) OR (c.user1 = :u2 AND c.user2 = :u1)")
    Conversation findConversationBetweenUsers(@Param("u1") User u1, @Param("u2") User u2);
}
