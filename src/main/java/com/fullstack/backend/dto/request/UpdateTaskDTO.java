package com.fullstack.backend.dto.request;

import com.fullstack.backend.entity.TaskPriority;
import com.fullstack.backend.entity.TaskType;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTaskDTO {

    @Size(max = 500, message = "Title cannot exceed 500 characters")
    private String title;

    @Size(max = 5000, message = "Description cannot exceed 5000 characters")
    private String description;

    private TaskType type;

    private TaskPriority priority;

    private Long assignedToId;  // Can be set to null to unassign

    private LocalDate dueDate;  // Can be set to null to remove due date

    // Note: status is updated via separate endpoint (PATCH /tasks/{id}/status)
    // Note: projectId cannot be changed (tasks belong to one project)
    // Note: createdBy cannot be changed (immutable)
    // Note: parentTaskId cannot be changed after creation (maintains hierarchy integrity)
}