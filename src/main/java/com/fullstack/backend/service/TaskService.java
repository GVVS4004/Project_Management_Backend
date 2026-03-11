package com.fullstack.backend.service;

import com.fullstack.backend.dto.request.CreateTaskDTO;
import com.fullstack.backend.dto.request.UpdateTaskDTO;
import com.fullstack.backend.dto.response.TaskResponseDTO;
import com.fullstack.backend.entity.TaskPriority;
import com.fullstack.backend.entity.TaskStatus;
import com.fullstack.backend.entity.TaskType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    // ========== CRUD OPERATIONS ==========

    TaskResponseDTO createTask(CreateTaskDTO dto);
    TaskResponseDTO getTaskById(Long id);
    TaskResponseDTO updateTask(Long id, UpdateTaskDTO dto);
    void deleteTask(Long id);

    // ========== STATUS MANAGEMENT ==========

    TaskResponseDTO updateTaskStatus(Long id, TaskStatus newStatus);

    // ========== QUERY OPERATIONS ==========

    Page<TaskResponseDTO> getTasksByProject(Long projectId, Optional<List<TaskType>> taskTypes, Pageable pageable);
    Page<TaskResponseDTO> getTasksByAssignee(Long userId, Pageable pageable);
    Page<TaskResponseDTO> getTasksByCreator(Long userId, Pageable pageable);
    Page<TaskResponseDTO> getMyTasks(Pageable pageable);
    List<TaskResponseDTO> getChildTasks(Long parentTaskId);

    // ========== FILTER OPERATIONS ==========

    Page<TaskResponseDTO> getTasksByStatus(TaskStatus status, Pageable pageable);
    Page<TaskResponseDTO> getTasksByPriority(TaskPriority priority, Pageable pageable);
    Page<TaskResponseDTO> getTasksByType(TaskType type, Pageable pageable);
    Page<TaskResponseDTO> getTasksByProjectAndStatus(Long projectId, TaskStatus status, Pageable pageable);
    Page<TaskResponseDTO> getTasksByProjectAndPriority(Long projectId, TaskPriority priority, Pageable pageable);
    Page<TaskResponseDTO> getTasksByAssigneeAndStatus(Long userId, TaskStatus status, Pageable pageable);

    // ========== SEARCH OPERATIONS ==========

    Page<TaskResponseDTO> searchTasksByTitle(String title, Pageable pageable);
    Page<TaskResponseDTO> searchTasks(String searchTerm, Pageable pageable);

    // ========== SPECIALIZED QUERIES ==========

    List<TaskResponseDTO> getOverdueTasks();
    List<TaskResponseDTO> getAllEpics();
    List<TaskResponseDTO> getUnassignedTasks(Long projectId);

    // ========== TASK ASSIGNMENT ==========

    TaskResponseDTO assignTask(Long taskId, Long userId);
    TaskResponseDTO unassignTask(Long taskId);
}