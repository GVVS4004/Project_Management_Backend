package com.fullstack.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentResponseDTO {
    private Long id;
    private String fileName;
    private Long fileSize;
    private String contentType;
    private Long taskId;
    private UserSummaryDTO uploadedBy;
    private LocalDateTime createdAt;
}
