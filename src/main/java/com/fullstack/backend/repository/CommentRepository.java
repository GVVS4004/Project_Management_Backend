package com.fullstack.backend.repository;

import com.fullstack.backend.entity.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    String COMMENT_GRAPH = "Comment.withRelationships";

    @EntityGraph(value = COMMENT_GRAPH)
    Optional<Comment> findById(Long id);

    // Get all comments for a task (chronological)
    List<Comment> findByTaskIdOrderByCreatedAtAsc(Long taskId);

    // Get top-level comments only (depth = 0)
    @EntityGraph(value = COMMENT_GRAPH)
    List<Comment> findByTaskIdAndDepthOrderByCreatedAtAsc(Long taskId, Integer depth);

    // Get replies to a specific comment (chronological)
    List<Comment> findByParentCommentIdOrderByCreatedAtAsc(Long parentCommentId);

    // Get all comments by an author (newest first)
    List<Comment> findByAuthorIdOrderByCreatedAtDesc(Long authorId);

    // Get non-deleted comments for a task (chronological)
    List<Comment> findByTaskIdAndIsDeletedOrderByCreatedAtAsc(Long taskId, Boolean isDeleted);

    // ========== COUNT QUERIES ==========

    // Count total comments on a task
    Long countByTaskId(Long taskId);

    Long countByParentCommentId(Long parentCommentId);
    // Count non-deleted comments
    Long countByTaskIdAndIsDeleted(Long taskId, Boolean isDeleted);

    // ========== EXISTS QUERIES ==========

    // Check if task has any comments
    Boolean existsByTaskId(Long taskId);
}
