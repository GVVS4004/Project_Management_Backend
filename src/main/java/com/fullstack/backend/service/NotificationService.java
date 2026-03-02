package com.fullstack.backend.service;

import com.fullstack.backend.dto.response.NotificationResponseDTO;

import java.util.List;

public interface NotificationService {

    List<NotificationResponseDTO> getMyNotifications();
    List<NotificationResponseDTO> getMyUnreadNotifications();
    Long getUnreadCount();
    NotificationResponseDTO markAsRead(Long notificationId);
    int markAllAsRead();
    void deleteNotification(Long notificationId);

    void notifyTaskAssigned(Long taskId, Long assigneeId);
    void notifyStatusChanged(Long taskId, String oldStatus, String newStatus);
    void notifyCommentOnTask(Long commentId, Long taskId);
    void notifyMentioned(Long commentId, Long taskId, List<String> mentionedUsernames);
}