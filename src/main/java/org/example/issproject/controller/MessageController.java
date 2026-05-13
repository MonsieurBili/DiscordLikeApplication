package org.example.issproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.issproject.domain.Conversation;
import org.example.issproject.domain.Message;
import org.example.issproject.domain.User;
import org.example.issproject.repository.ConversationRepository;
import org.example.issproject.repository.UserRepository;
import org.example.issproject.request.MessageRequest;
import org.example.issproject.service.MessageService;
import org.example.issproject.service.UserService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.UUID;

@Controller
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final UserService userService;
    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat")
    public void sendMessage(@Payload MessageRequest messageRequest, Principal user) {
        UUID idReceiver = messageRequest.getUser1();

        User receiver = userService.findById(idReceiver);
        if (receiver == null) {
            log.warn("Receiver not found for id: {}", idReceiver);
            return;
        }

        User sender = userService.findByUsername(user.getName());

        Message message = new Message();
        message.setContent(messageRequest.getMessage());
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setTimestamp(LocalDateTime.now());

        message = messageService.save(message);

        messagingTemplate.convertAndSendToUser(
                receiver.getUsername(),
                "/queue/messages",
                message
        );
        log.info("Message delivered to receiver: {}", receiver.getUsername());

        messagingTemplate.convertAndSendToUser(
                sender.getUsername(),
                "/queue/messages",
                message
        );
        log.info("Echo delivered to sender: {}", sender.getUsername());
    }
}