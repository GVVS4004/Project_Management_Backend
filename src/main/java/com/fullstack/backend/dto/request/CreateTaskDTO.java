package com.fullstack.backend.dto.request;

import com.fullstack.backend.entity.TaskPriority;
import com.fullstack.backend.entity.TaskType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTaskDTO {

    @NotBlank(message = "Task title is required")
    @Size(max = 500, message = "Title cannot exceed 500 characters")
    private String title;

    @Size(max = 5000, message = "Description cannot exceed 5000 characters")
    private String description;

    @NotNull(message = "Project ID is required")
    private Long projectId;

    private TaskType type;  // Optional, defaults to TASK in entity

    private TaskPriority priority;  // Optional, defaults to MEDIUM in entity

    private Long assignedToId;  // Optional - can be null (unassigned)

    private Long parentTaskId;  // Optional - only for subtasks or stories under epic

    private LocalDate dueDate;  // Optional
}