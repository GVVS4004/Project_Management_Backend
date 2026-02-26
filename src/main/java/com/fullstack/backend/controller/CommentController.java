package com.fullstack.backend.controller;

import com.fullstack.backend.dto.request.CreateCommentDTO;
import com.fullstack.backend.dto.request.UpdateCommentDTO;
import com.fullstack.backend.dto.response.ApiResponse;
import com.fullstack.backend.dto.response.CommentResponseDTO;
import com.fullstack.backend.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{taskId}/comments")
    public ResponseEntity<ApiResponse<CommentResponseDTO>> createComment(@PathVariable Long taskId, @RequestBody @Valid CreateCommentDTO dto){
        CommentResponseDTO responseDTO = commentService.createComment(dto,taskId);
        return ResponseEntity.ok(ApiResponse.success("Comment created successfully",responseDTO));
    }

//    @PostMapping("/{taskId}/comments/reply")
//    public ResponseEntity<ApiResponse<CommentResponseDTO>> createReply(@PathVariable Long taskId, @RequestBody @Valid CreateCommentDTO dto){
//        CommentResponseDTO responseDTO = commentService.createComment(dto,taskId);
//        return ResponseEntity.ok(ApiResponse.success("Comment created successfully",responseDTO));
//    }

    @GetMapping("/{taskId}/comments")
    public ResponseEntity<ApiResponse<List<CommentResponseDTO>>> getAllComments(@PathVariable Long taskId){
        List<CommentResponseDTO> responseDTOS = commentService.getCommentsByTaskId(taskId);
        return ResponseEntity.ok(ApiResponse.success("Comment fetched successfully",responseDTOS));
    }

    @GetMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponseDTO>> getComment(@PathVariable Long commentId){
        CommentResponseDTO responseDTO = commentService.getCommentById(commentId);
        return ResponseEntity.ok(ApiResponse.success("Comment fetched successfully",responseDTO));
    }

    @PutMapping("/{taskId}/comments/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponseDTO>> updateComment(@PathVariable Long taskId,@PathVariable Long commentId, @RequestBody @Valid UpdateCommentDTO dto){
        CommentResponseDTO responseDTO = commentService.updateComment(dto,commentId);
        return ResponseEntity.ok(ApiResponse.success("Comment updated successfully",responseDTO));
    }

    @DeleteMapping("/{taskId}/comments/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponseDTO>> deleteComment(@PathVariable Long taskId,@PathVariable Long commentId){
        commentService.deleteComment(commentId);
        return ResponseEntity.ok(ApiResponse.success("Comment deleted successfully",null));
    }
}
