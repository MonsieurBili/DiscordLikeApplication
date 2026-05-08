package org.example.issproject.request;

import lombok.Data;
import org.example.issproject.domain.ActivityStatus;

@Data
public class UpdateProfileRequest {
    private String description;
    private ActivityStatus activity;
}
