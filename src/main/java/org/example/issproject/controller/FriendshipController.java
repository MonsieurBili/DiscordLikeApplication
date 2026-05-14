    package org.example.issproject.controller;

    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.example.issproject.domain.Conversation;
    import org.example.issproject.domain.Friendship;
    import org.example.issproject.domain.User;
    import org.example.issproject.service.ConversationService;
    import org.example.issproject.service.FriendshipService;
    import org.example.issproject.service.UserService;
    import org.springframework.data.repository.query.Param;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.core.annotation.AuthenticationPrincipal;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;
    import java.util.UUID;

    @RestController
    @CrossOrigin(origins = {"http://localhost:5173","https://uiiss.onrender.com/"})
    @RequestMapping("api/friends")
    @RequiredArgsConstructor
    @Slf4j
    public class FriendshipController {

        private final UserService userService;
        private final FriendshipService friendshipService;
        private final ConversationService conversationService;

        @GetMapping("")
        public ResponseEntity<List<Friendship>>getCurrentFriendships(@AuthenticationPrincipal User user)
        {
            log.info("Get Current Friendships method called in controller");
            List<Friendship> friendships = friendshipService.getUsersFriendshipsAccepted(user);
            log.info("Found {} friendships for user {}", friendships.size(), user.getUsername());
            if (friendships.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(friendships, HttpStatus.OK);
        }

        @PostMapping("/sendFriendRequest/{username}")
        public ResponseEntity<Friendship>sendFriendRequest(@AuthenticationPrincipal User user, @PathVariable String username)
        {
            try {
                User receiver = userService.findByUsername(username);
                friendshipService.sendFriendshipRequest(user,receiver);

            }catch (Exception e)
            {
                log.info("Failed in controller {}",e.getMessage());
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }

        @PostMapping("/{friendRequest}/accept")
        public ResponseEntity<Void>acceptFriendRequest(@AuthenticationPrincipal User user, @PathVariable UUID friendRequest)
        {
            try {
                log.info("Accept Friendship request {}",friendRequest);
                org.example.issproject.domain.Friendship friendship = friendshipService.findById(friendRequest);
                if (!friendship.getUser2().getId().equals(user.getId())) {
                    log.warn("User try to intervene in someone elses friendship {}", user.getUsername());
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                }
                friendshipService.acceptFriendshipRequest(friendship);
                log.info("Accepted succesfully");
                Conversation convo = new Conversation();
                convo.setUser1(friendship.getUser1());
                convo.setUser2(friendship.getUser2());
                conversationService.save(convo);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            catch (Exception e)
            {
                log.info("Failed in controller {}",e.getMessage());
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        }

        @GetMapping("/friendsRequests")
        public ResponseEntity<List<Friendship>>getFriendRequests(@AuthenticationPrincipal User user)
        {
            log.info("Get Current Friendships method called in controller");
            List<Friendship> friendships = friendshipService.getUsersFriendshipsRequested(user);
            log.info("Found {} friendships for user {}", friendships.size(), user.getUsername());
            if (friendships.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(friendships, HttpStatus.OK);
        }


        @PostMapping("/{friendRequest}/deny")
        public ResponseEntity<Void>denyFriendRequest(@AuthenticationPrincipal User user, @PathVariable UUID friendRequest)
        {
            try {
                log.info("Deny Friendship request {}",friendRequest);
                org.example.issproject.domain.Friendship friendship = friendshipService.findById(friendRequest);
                if (!friendship.getUser2().getId().equals(user.getId())) {
                    log.warn("User try to intervene in someone elses friendship {}", user.getUsername());
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                }
                friendshipService.denyFriendRequest(friendship);
                log.info("Denied succesfully");
                return new ResponseEntity<>(HttpStatus.OK);
            }
            catch (Exception e)
            {
                log.info("Failed in controller {}",e.getMessage());
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        }
    }
