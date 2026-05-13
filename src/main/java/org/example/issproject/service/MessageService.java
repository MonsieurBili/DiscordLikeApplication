package org.example.issproject.service;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.apache.bcel.Repository;
import org.example.issproject.domain.Conversation;
import org.example.issproject.domain.Message;
import org.example.issproject.repository.ConversationRepository;
import org.example.issproject.repository.MessageRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;

    public Message save(Message message) {
        if (message == null) {
            throw  new IllegalArgumentException("message is null");
        }
        else {
            Conversation conversation1 = conversationRepository.findConversationByUser1AndUser2(message.getSender(), message.getReceiver());
            Conversation conversation2 = conversationRepository.findConversationByUser1AndUser2(message.getReceiver(), message.getSender());
            if(conversation1 == null && conversation2 == null) {
                Conversation conversation = new Conversation();
                conversation.setUser1(message.getSender());
                conversation.setUser2(message.getReceiver());
                conversation.setLastMessagePreview(message.getContent());
                conversation.setLastMessageTime(LocalDateTime.now());
                conversation = conversationRepository.save(conversation);
                message.setConversation(conversation);
            }
            else {
                if(conversation1 != null) {
                    message.setConversation(conversation1);
                    conversation1.setLastMessagePreview(message.getContent());
                    conversation1.setLastMessageTime(LocalDateTime.now());
                    conversationRepository.save(conversation1);

                }
                else {
                    message.setConversation(conversation2);
                    conversation2.setLastMessagePreview(message.getContent());
                    conversation2.setLastMessageTime(LocalDateTime.now());
                    conversationRepository.save(conversation2);
                }
                message = messageRepository.save(message);
            }
        }
        return message;
    }

    public List<Message> getMessages(Conversation conversation,int page,int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        List<Message> messages = messageRepository.findLatestMessages(conversation, pageable);
        return messages;

    }

}
