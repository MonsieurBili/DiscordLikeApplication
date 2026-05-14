package org.example.issproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.issproject.domain.Conversation;
import org.example.issproject.domain.Message;
import org.example.issproject.domain.User;
import org.example.issproject.service.ConversationService;
import org.example.issproject.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = {"http://localhost:5173","https://uiiss.onrender.com/"})
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
@Slf4j
public class ConversationController {

    private final ConversationService conversationService;
    private final MessageService messageService;

    @GetMapping
    public ResponseEntity<List<Conversation>> getMyConversations(@AuthenticationPrincipal User user) {
        log.info("Fetching conversations for user: {}", user.getUsername());

        List<Conversation> conversations = conversationService.getUserConversations(user);

        if (conversations.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(conversations, HttpStatus.OK);
    }


    @GetMapping("/{conversation}")
    public ResponseEntity<List<Message>> getMessages(
            @AuthenticationPrincipal User user,
            @PathVariable UUID conversation,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "500") int size
    ) {
        log.info("User {} loading messages for conversation {}", user.getUsername(), conversation);
        Conversation conv = conversationService.findById(conversation);
        if (conv == null) {
            return ResponseEntity.notFound().build();
        }
        List<Message> messages = messageService.getMessages(conv, page, size);
        return ResponseEntity.ok(messages);
    }


}