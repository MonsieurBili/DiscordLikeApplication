package org.example.issproject.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name="profiles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Profile {
    @Id
    private UUID id;

    @Column(name = "profile_pic_url")
    private String profilePicUrl;

    @Column(name="description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity_status", nullable = false)
    private ActivityStatus activity;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;


}
