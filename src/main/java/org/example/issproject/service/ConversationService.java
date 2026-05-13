package org.example.issproject.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.issproject.domain.Conversation;
import org.example.issproject.domain.Message;
import org.example.issproject.domain.User;
import org.example.issproject.repository.ConversationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;

    public List<Conversation> getUserConversations(User user) {
        return conversationRepository.findByUserOrderByLastMessageTimeDesc(user);
    }
    public Conversation findById(UUID id) {
        return conversationRepository.findById(id).orElse(null);
    }

    public Conversation save(Conversation conversation) {
        return conversationRepository.save(conversation);
    }
}
