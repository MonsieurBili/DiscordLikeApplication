package org.example.issproject.repository;

import org.example.issproject.domain.Conversation;
import org.example.issproject.domain.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    @Query("SELECT m FROM Message m WHERE m.conversation = :conversation ORDER BY m.timestamp asc")
    List<Message> findLatestMessages(@Param("conversation") Conversation conversation, Pageable pageable);
}
