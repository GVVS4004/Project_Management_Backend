package com.fullstack.backend.repository;

import com.fullstack.backend.entity.Notification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    String NOTIFICATION_GRAPH = "Notification.withRelationships";

    @EntityGraph(value = NOTIFICATION_GRAPH)
    List<Notification> findByRecipientIdOrderByCreatedAtDesc(Long recipientId);

    @EntityGraph(value = NOTIFICATION_GRAPH)
    List<Notification> findByRecipientIdAndIsReadOrderByCreatedAtDesc(Long recipientId, Boolean isRead);

    Long countByRecipientIdAndIsRead(Long recipientId, Boolean isRead);

    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.recipient.id = :recipientId AND n.isRead = false")
    int markAllAsRead(Long recipientId);
}
