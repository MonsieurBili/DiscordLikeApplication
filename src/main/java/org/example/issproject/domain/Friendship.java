package org.example.issproject.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.mapping.Join;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="friendships")
public class Friendship {

    public Friendship(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
        this.setStatus(FriendshipStatus.PENDING);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name="user1_id",nullable = false)
    private User user1;

    @ManyToOne
    @JoinColumn(name="user2_id",nullable = false)
    private User user2;

    @Enumerated(EnumType.STRING)
    @Column(name="friendshipStatus")
    private FriendshipStatus status;

    @Column(name="friendsSince")
    private LocalDate friendsSince;
}
