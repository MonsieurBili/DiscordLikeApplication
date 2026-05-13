package org.example.issproject.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.issproject.domain.Friendship;
import org.example.issproject.domain.FriendshipStatus;
import org.example.issproject.domain.User;
import org.example.issproject.repository.FriendshipRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;
    private  final UserService userService;

    public Friendship sendFriendshipRequest(User sender, User receiver) {
        log.info("Sending friendship request");
        if(sender.getId()==receiver.getId() || sender.getId()==null || receiver.getId()==null){
            log.info("Sending friendship request failed sender or receiver didn't do well");
            throw new RuntimeException("Friendship failed sender or receiver didn't do well");
        }
        if (friendshipRepository.friendshipExistsBetween(sender, receiver)) {
            log.info("Friendship request failed: relation already exists between {} and {}",
                    sender.getUsername(), receiver.getUsername());
            throw new RuntimeException("Friendship already exists");
        }
            Friendship friendship = new Friendship(sender, receiver);
            friendshipRepository.save(friendship);
            return friendship;

    }

    public Friendship acceptFriendshipRequest(Friendship friendship) {
        log.info("Accepting friendship request");
        if (friendship.getId()==null) {
            log.info("Sending friendship request failed friendship id is null");
            return null;
        }
        friendship.setStatus(FriendshipStatus.ACCEPTED);
        friendship.setFriendsSince(LocalDate.now());
        friendshipRepository.save(friendship);
        log.info("Friendship accepted successfully");
        return friendship;
    }


    public Friendship denyFriendRequest(Friendship friendship) {
        log.info("Accepting friendship request");
        if (friendship.getId()==null) {
            log.info("Sending friendship request failed friendship id is null");
            return null;
        }
        friendship.setStatus(FriendshipStatus.ACCEPTED);
        friendship.setFriendsSince(LocalDate.now());
        friendshipRepository.save(friendship);
        log.info("Friendship accepted successfully");
        return friendship;
    }

    public List<Friendship>getUsersFriendshipsAccepted(User user)
    {
        log.info("Getting friendships accepted for user {}", user.getUsername());
        return friendshipRepository.findByUserAndStatus(user, FriendshipStatus.ACCEPTED);
    }


    public List<Friendship>getUsersFriendshipsRequested(User user)
    {
        log.info("Getting friendships accepted for user {}", user.getUsername());
        return friendshipRepository.findByUserAndStatus(user, FriendshipStatus.PENDING);
    }

    public Friendship findById(UUID ID)
    {
        Friendship friendship = friendshipRepository.findById(ID).get();
        if(friendship==null){
            throw new RuntimeException("Friendship not found");
        }
        else
            return  friendship;
    }


//    public List<Friendship>getUsersFriendshipsPending(User user)
//    {
//        log.info("Getting friendships pending for user {}", user.getUsername());
//        return user.getAllFriendships().stream().filter(friendship -> friendship.getStatus()==FriendshipStatus.PENDING).toList();
//    }


}
