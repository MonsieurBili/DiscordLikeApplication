package org.example.issproject.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="friendships")
public class Friendship {

    @Id
    private UUID id;
    @ManyToOne
    private User user1;

    @ManyToOne(ma)
    private User user2;

    private LocalDate friendsSince;
}
