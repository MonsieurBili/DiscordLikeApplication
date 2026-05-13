package org.example.issproject.websocket;

import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.issproject.domain.ActivityStatus;
import org.example.issproject.domain.Profile;
import org.example.issproject.domain.User;
import org.example.issproject.service.ProfileService;
import org.example.issproject.service.UserService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ProfileService profileService;


    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        Principal userPrincipal = event.getUser();

        if (userPrincipal != null) {
            String username = userPrincipal.getName();
            log.info("Userul {} a intrat ONLINE", username);

             User user = userService.findByUsername(username);
             Profile userProfile = user.getProfile();
             userProfile.setActivity(ActivityStatus.ONLINE);
             profileService.save(userProfile);
             log.info("{}",userProfile);
            messagingTemplate.convertAndSend("/topic/public", username + " este ONLINE");
        }
    }



    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        Principal userPrincipal = event.getUser();

        if (userPrincipal != null) {
            String username = userPrincipal.getName();
            log.info("Userul {} a ieșit OFFLINE", username);

            User user = userService.findByUsername(username);
            Profile userProfile = user.getProfile();
            userProfile.setActivity(ActivityStatus.OFFLINE);
            profileService.save(userProfile);

            messagingTemplate.convertAndSend("/topic/public", username + " este OFFLINE");
        }
    }


}
