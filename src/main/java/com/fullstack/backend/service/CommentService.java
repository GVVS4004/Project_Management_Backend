package com.fullstack.backend.service;

import com.fullstack.backend.dto.request.CreateCommentDTO;
import com.fullstack.backend.dto.request.UpdateCommentDTO;
import com.fullstack.backend.dto.response.CommentResponseDTO;

import java.util.List;

public interface CommentService {
    CommentResponseDTO createComment(CreateCommentDTO dto, Long taskId);
    CommentResponseDTO updateComment(UpdateCommentDTO dto, Long commentId);
    void deleteComment(Long commentId);
    CommentResponseDTO getCommentById(Long commentId);
    List<CommentResponseDTO> getCommentsByTaskId(Long taskId);
    Long getCommentCount(Long taskId);
    Long getRepliesCount(Long commentId);
}
