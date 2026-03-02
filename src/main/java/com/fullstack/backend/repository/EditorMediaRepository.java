package com.fullstack.backend.repository;

import com.fullstack.backend.entity.EditorMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EditorMediaRepository extends JpaRepository<EditorMedia, UUID> {
    List<EditorMedia> findByTaskId(Long taskId);

    List<EditorMedia> findByCommentId(Long commentId);

    List<EditorMedia> findByIdIn(List<UUID> ids);

    List<EditorMedia> findByTaskIdAndCommentIdIsNull(Long taskId);
}
