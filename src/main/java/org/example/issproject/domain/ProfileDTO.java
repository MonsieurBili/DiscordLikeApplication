package org.example.issproject.domain;

import lombok.Data;

@Data
public class ProfileDTO {
    private String profilePicUrl;
    private String description;
    private ActivityStatus activity;
    private String username;

    public ProfileDTO(String profilePicUrl, String description, ActivityStatus activity, String username) {
        this.profilePicUrl = profilePicUrl;
        this.description = description;
        this.activity = activity;
        this.username = username;
    }
}
