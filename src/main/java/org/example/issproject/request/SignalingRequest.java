package org.example.issproject.request;

import lombok.Data;

@Data
public class SignalingRequest {
    private String targetUsername;
    private String senderUsername;
    private String type;

    private Object payload;
}
