package com.fullstack.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {
    private String message;
    private LocalDateTime timestamp;

    public static MessageResponse of(String message){
        return new MessageResponse(message, LocalDateTime.now());
    }

    public static MessageResponse success(String message){
        return new MessageResponse(message, LocalDateTime.now());
    }

    public static MessageResponse failed(String message){
        return new MessageResponse(message, LocalDateTime.now());
    }
}
