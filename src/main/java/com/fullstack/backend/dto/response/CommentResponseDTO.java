package com.fullstack.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDTO {
    private Long id;
    private String content;
    private UserSummaryDTO author;
    private Long parentCommentId;
    private List<CommentResponseDTO> replies;
    private Boolean isEdited;
    private Boolean isDeleted;
    private LocalDateTime deletedAt;
    private Long deletedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer depth;
    private Long taskId;
    private Integer replyCount;
}
