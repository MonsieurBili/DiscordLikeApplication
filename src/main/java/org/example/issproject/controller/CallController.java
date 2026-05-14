package org.example.issproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.issproject.request.SignalingRequest;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CallController {
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/call/signal")
    public void handleSignaling(@Payload SignalingRequest request, Principal principal) {
        String senderUsername = principal.getName();

        messagingTemplate.convertAndSendToUser(
                request.getTargetUsername(),
                "/queue/call",
                request
        );

        log.info("Call signal send from {} to {}", senderUsername, request.getTargetUsername());
    }
}

