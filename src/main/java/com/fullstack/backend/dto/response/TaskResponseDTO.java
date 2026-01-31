package com.fullstack.backend.dto.response;

import com.fullstack.backend.entity.TaskPriority;
import com.fullstack.backend.entity.TaskStatus;
import com.fullstack.backend.entity.TaskType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseDTO {

    private Long id;
    private String title;
    private String description;

    private TaskStatus status;
    private TaskPriority priority;
    private TaskType type;

    private Long projectId;
    private String projectName;  // Convenient for display

    private UserSummaryDTO createdBy;
    private UserSummaryDTO assignedTo;  // Can be null

    private Long parentTaskId;  // Can be null for top-level tasks
    private String parentTaskTitle;  // Convenient for display

    private LocalDate dueDate;  // Can be null
    private LocalDateTime completedAt;  // Null until task is completed

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}