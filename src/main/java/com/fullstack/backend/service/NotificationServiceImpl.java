package com.fullstack.backend.service;

import com.fullstack.backend.dto.response.NotificationResponseDTO;
import com.fullstack.backend.entity.*;
import com.fullstack.backend.exception.ResourceNotFoundException;
import com.fullstack.backend.repository.CommentRepository;
import com.fullstack.backend.repository.NotificationRepository;
import com.fullstack.backend.repository.TaskRepository;
import com.fullstack.backend.repository.UserRepository;
import com.fullstack.backend.util.SecurityUtils;
import com.fullstack.backend.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService{
    private final NotificationRepository notificationRepository;
    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;


    private Notification createNotification(User recipient, User actor, NotificationType type,
                                            Task task, Comment comment, String message) {
        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setActor(actor);
        notification.setType(type);
        notification.setTask(task);
        notification.setComment(comment);
        notification.setMessage(message);
        return notificationRepository.save(notification);
    }

    private NotificationResponseDTO convertToDTO(Notification notification) {
        NotificationResponseDTO dto = new NotificationResponseDTO();
        dto.setId(notification.getId());
        dto.setType(notification.getType());
        dto.setMessage(notification.getMessage());
        dto.setIsRead(notification.getIsRead());
        dto.setActor(UserMapper.toSummaryDTO(notification.getActor()));
        dto.setTaskId(notification.getTask() != null ? notification.getTask().getId() : null);
        dto.setCommentId(notification.getComment() != null ? notification.getComment().getId() : null);
        dto.setCreatedAt(notification.getCreatedAt());
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponseDTO> getMyNotifications(){

        User currentUser = SecurityUtils.getCurrentUser();

        return notificationRepository.findByRecipientIdOrderByCreatedAtDesc(currentUser.getId()).stream().map(this::convertToDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponseDTO> getMyUnreadNotifications() {
        User currentUser = SecurityUtils.getCurrentUser();
        return notificationRepository.findByRecipientIdAndIsReadOrderByCreatedAtDesc(currentUser.getId(), false)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Long getUnreadCount() {
        User currentUser = SecurityUtils.getCurrentUser();
        return notificationRepository.countByRecipientIdAndIsRead(currentUser.getId(), false);
    }

    @Override
    public NotificationResponseDTO markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + notificationId));
        notification.setIsRead(true);
        notificationRepository.save(notification);
        return convertToDTO(notification);
    }

    @Override
    public int markAllAsRead() {
        User currentUser = SecurityUtils.getCurrentUser();
        return notificationRepository.markAllAsRead(currentUser.getId());
    }

    @Override
    public void deleteNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + notificationId));
        notificationRepository.delete(notification);
    }

    @Override
    public void notifyTaskAssigned(Long taskId, Long assigneeId){
        User actor = SecurityUtils.getCurrentUser();

        if(actor.getId().equals(assigneeId)) return;

        Task task = taskRepository.findById(taskId).orElseThrow(()-> new ResourceNotFoundException("Task not found with id: "+ taskId));
        User assignee = userRepository.findById(assigneeId).orElseThrow(()-> new ResourceNotFoundException("User not found with id: "+ assigneeId));
        Notification notification = new Notification();
        notification.setRecipient(assignee);
        notification.setActor(actor);
        notification.setType(NotificationType.TASK_ASSIGNED);
        notification.setTask(task);
        notification.setMessage(actor.getFirstName() + " assigned you to \"" + task.getTitle() + "\"");

        notificationRepository.save(notification);
    }


    @Override
    public void notifyStatusChanged(Long taskId, String oldStatus, String newStatus) {
        User actor = SecurityUtils.getCurrentUser();

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));


        if (task.getAssignedTo() != null && !task.getAssignedTo().getId().equals(actor.getId())) {
            createNotification(
                    task.getAssignedTo(), actor, NotificationType.STATUS_CHANGED, task, null,
                    actor.getFirstName() + " changed \"" + task.getTitle() + "\" from " + oldStatus + " to " + newStatus
            );
        }

        if (!task.getCreatedBy().getId().equals(actor.getId())
                && (task.getAssignedTo() == null || !task.getCreatedBy().getId().equals(task.getAssignedTo().getId()))) {
            createNotification(
                    task.getCreatedBy(), actor, NotificationType.STATUS_CHANGED, task, null,
                    actor.getFirstName() + " changed \"" + task.getTitle() + "\" from " + oldStatus + " to " + newStatus
            );
        }
    }

    @Override
    public void notifyCommentOnTask(Long commentId, Long taskId) {
        User actor = SecurityUtils.getCurrentUser();

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));

        if (!task.getCreatedBy().getId().equals(actor.getId())) {
            createNotification(
                    task.getCreatedBy(), actor, NotificationType.COMMENT_ON_YOUR_TASK, task, comment,
                    actor.getFirstName() + " commented on \"" + task.getTitle() + "\""
            );
        }
    }

    @Override
    public void notifyMentioned(Long commentId, Long taskId, List<String> mentionedUsernames) {
        if (mentionedUsernames == null || mentionedUsernames.isEmpty()) return;

        User actor = SecurityUtils.getCurrentUser();

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));

        for (String username : mentionedUsernames) {
            userRepository.findByUserName(username).ifPresent(user -> {
                // Don't notify yourself
                if (!user.getId().equals(actor.getId())) {
                    createNotification(
                            user, actor, NotificationType.MENTIONED_IN_COMMENT, task, comment,
                            actor.getFirstName() + " mentioned you in \"" + task.getTitle() + "\""
                    );
                }
            });
        }
    }
}
