package com.fullstack.backend.dto.response;

import com.fullstack.backend.entity.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponseDTO {
    private Long id;
    private NotificationType type;
    private String message;
    private Boolean isRead;
    private UserSummaryDTO actor;
    private Long taskId;
    private Long commentId;
    private LocalDateTime createdAt;
}