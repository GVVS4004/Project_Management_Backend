package com.fullstack.backend.repository;

import com.fullstack.backend.entity.Attachment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    String ATTACHMENT_GRAPH = "Attachment.withRelationships";

    @EntityGraph(value = ATTACHMENT_GRAPH)
    List<Attachment> findByTaskIdOrderByCreatedAtDesc(Long taskId);

    @EntityGraph(value = ATTACHMENT_GRAPH)
    Optional<Attachment> findById(Long id);

    @EntityGraph(value = ATTACHMENT_GRAPH)
    List<Attachment> findByUploadedByIdOrderByCreatedAtDesc(Long userId);

    Long countByTaskId(Long taskId);

    Boolean existsByTaskId(Long taskId);

    void deleteAllByTaskId(Long taskId);
}
