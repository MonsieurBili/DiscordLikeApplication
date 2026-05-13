package org.example.issproject.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.issproject.domain.User;
import org.example.issproject.security.JWTService;
import org.example.issproject.service.UserDetailsOwn;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@RequiredArgsConstructor
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketSecurityConfig implements WebSocketMessageBrokerConfigurer {

    private final JWTService jwtService;
    private final UserDetailsOwn userDetailsOwn;

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    log.info("Interceptez cererea de conectare WebSocket...");

                    String authHeader = accessor.getFirstNativeHeader("Authorization");

                    if (authHeader != null && authHeader.startsWith("Bearer ")) {
                        String token = authHeader.substring(7);

                        try {
                            String username = jwtService.extractUsername(token);

                            if (username != null) {
                                User userDetails = userDetailsOwn.loadUserByUsername(username);

                                if (jwtService.validateToken(token, userDetails)) {
                                    UsernamePasswordAuthenticationToken authentication =
                                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                                    SecurityContextHolder.getContext().setAuthentication(authentication);
                                    accessor.setUser(authentication);

                                    log.info("User-ul '{}' s-a conectat cu succes la WebSocket!", username);
                                } else {
                                    log.warn("Token invalid pentru utilizatorul: {}", username);
                                }
                            }
                        } catch (Exception e) {
                            log.error("Eroare la validarea token-ului în WebSocket: {}", e.getMessage());
                        }
                    } else {
                        log.warn("Conectare respinsă: Lipsește Token-ul JWT sau e formatat greșit!");
                    }
                }
                return message;
            }
        });
    }
}