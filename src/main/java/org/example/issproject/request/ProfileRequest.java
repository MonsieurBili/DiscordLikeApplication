package org.example.issproject.request;

import lombok.Data;
import org.example.issproject.domain.ActivityStatus;

@Data
public class ProfileRequest {
    private  String profilePicUrl;
    private String description;
    private ActivityStatus activityStatus;

}
