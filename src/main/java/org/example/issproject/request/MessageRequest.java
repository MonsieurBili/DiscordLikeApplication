package org.example.issproject.request;

import lombok.Data;

import java.util.UUID;

@Data
public class MessageRequest {
    private  String message;
    private UUID user1;
}
