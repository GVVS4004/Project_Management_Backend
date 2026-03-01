package com.fullstack.backend.service;


import com.fullstack.backend.dto.request.CreateCommentDTO;
import com.fullstack.backend.dto.request.UpdateCommentDTO;
import com.fullstack.backend.dto.response.CommentResponseDTO;
import com.fullstack.backend.entity.Comment;
import com.fullstack.backend.entity.Role;
import com.fullstack.backend.entity.Task;
import com.fullstack.backend.entity.User;
import com.fullstack.backend.exception.BadRequestException;
import com.fullstack.backend.exception.ResourceNotFoundException;
import com.fullstack.backend.exception.ForbiddenException;
import com.fullstack.backend.repository.CommentRepository;
import com.fullstack.backend.repository.TaskRepository;
import com.fullstack.backend.util.SecurityUtils;
import com.fullstack.backend.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;

    private CommentResponseDTO convertToCommentResponseDTO(Comment comment){
        CommentResponseDTO dto = new CommentResponseDTO();
        dto.setContent(comment.getIsDeleted() ? "[Comment deleted]" : comment.getContent());
        dto.setAuthor(UserMapper.toSummaryDTO(comment.getAuthor()));
        dto.setParentCommentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null);
        dto.setReplies(comment.getReplies().stream().map(this::convertToCommentResponseDTO).toList());
        dto.setDepth(comment.getDepth());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setDeletedAt(comment.getDeletedAt());
        dto.setDeletedBy(comment.getDeletedBy());
        dto.setTaskId(comment.getTask().getId());
        dto.setUpdatedAt(comment.getUpdatedAt());
        dto.setId(comment.getId());
        dto.setIsEdited(comment.getIsEdited());
        dto.setIsDeleted(comment.getIsDeleted());
        dto.setReplyCount(comment.getReplies().size());
        return dto;
    }
    private void checkCanEditComment(Comment comment){
        User currentUser = SecurityUtils.getCurrentUser();
        if(!comment.getAuthor().getId().equals(currentUser.getId())){
            throw new ForbiddenException("You are not the author of the comment");
        }
        if(comment.getIsDeleted()){
            throw new BadRequestException("You can not edit a deleted comment");
        }
    }

    private void checkCanDeleteComment(Comment comment){
        User currentUser = SecurityUtils.getCurrentUser();
        if(currentUser.getRole()!= Role.ADMIN && !comment.getAuthor().getId().equals(currentUser.getId())){
            throw new ForbiddenException("You cannot delete the comment");
        }
        if(comment.getIsDeleted()){
            throw new BadRequestException("Comment already deleted with id:" + comment.getId());
        }
    }
    @Override
    public CommentResponseDTO createComment(CreateCommentDTO dto, Long taskId){
        Task task = taskRepository.findById(taskId).orElseThrow(()-> new ResourceNotFoundException("Task not found with id: "+taskId));
        User currentUser = SecurityUtils.getCurrentUser();
        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        comment.setAuthor(currentUser);
        comment.setTask(task);
        if(dto.getParentCommentId()!=null){
            Comment parentComment = commentRepository.findById(dto.getParentCommentId()).orElseThrow(()-> new ResourceNotFoundException("Comment not found with id: "+ dto.getParentCommentId()));
            if (!parentComment.getTask().getId().equals(taskId)) {
                throw new BadRequestException("Parent comment belongs to a different task");
            }
            if (parentComment.getDepth() >= 1) {
                throw new BadRequestException("Cannot reply to a reply. Maximum nesting depth is 1 level.");
            }
            comment.setParentComment(parentComment);
        }
        commentRepository.save(comment);
        return convertToCommentResponseDTO(comment);
    }

    @Override
    public CommentResponseDTO updateComment(UpdateCommentDTO dto, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment not found with id:"+commentId));
        checkCanEditComment(comment);
        comment.setContent(dto.getContent());
        comment.setIsEdited(true);
        commentRepository.save(comment);
        return convertToCommentResponseDTO(comment);
    }

    @Override
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment not found with id:"+commentId));
        checkCanDeleteComment(comment);
        comment.setIsDeleted(true);
        comment.setDeletedAt(LocalDateTime.now());
        comment.setDeletedBy(SecurityUtils.getCurrentUser().getId());
        commentRepository.save(comment);
    }

    @Override
    public CommentResponseDTO getCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment not found with id:"+commentId));
        return convertToCommentResponseDTO(comment);
    }

    @Override
    public List<CommentResponseDTO> getCommentsByTaskId(Long taskId) {
        List<Comment> comments = commentRepository.findByTaskIdAndDepthOrderByCreatedAtAsc(taskId,0);
        return comments.stream().map(this::convertToCommentResponseDTO).toList();
    }

    @Override
    public Long getCommentCount(Long taskId) {
        return commentRepository.countByTaskId(taskId);
    }

    @Override
    public Long getRepliesCount(Long commentId) {
        return commentRepository.countByParentCommentId(commentId);
    }
}
