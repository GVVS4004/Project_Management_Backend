package com.fullstack.backend.controller;


import com.fullstack.backend.dto.request.CreateTaskDTO;
import com.fullstack.backend.dto.request.UpdateTaskDTO;
import com.fullstack.backend.dto.response.ApiResponse;
import com.fullstack.backend.dto.response.TaskResponseDTO;
import com.fullstack.backend.entity.TaskPriority;
import com.fullstack.backend.entity.TaskStatus;
import com.fullstack.backend.entity.TaskType;
import com.fullstack.backend.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping()
    public ResponseEntity<ApiResponse<TaskResponseDTO>> createTask(@Valid @RequestBody CreateTaskDTO dto){
        TaskResponseDTO result = taskService.createTask(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Task created successfully", result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponseDTO>> getTaskById(@PathVariable Long id){
        TaskResponseDTO result = taskService.getTaskById(id);
        return ResponseEntity.ok(ApiResponse.success("Task fetched successfully", result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponseDTO>> updateTask(@PathVariable Long id, @Valid @RequestBody UpdateTaskDTO dto){
        TaskResponseDTO result = taskService.updateTask(id,dto);
        return ResponseEntity.ok(ApiResponse.success("Task updated successfully", result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable Long id){
        taskService.deleteTask(id);
        return ResponseEntity.ok(ApiResponse.success("Task deleted successfully",null));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<TaskResponseDTO>> updateTaskStatus(@PathVariable Long id, @RequestParam TaskStatus status){
        TaskResponseDTO result = taskService.updateTaskStatus(id,status);
        return ResponseEntity.ok(ApiResponse.success("Task status updated successfully",result));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<Page<TaskResponseDTO>>> getMyTasks(Pageable pageable){
        Page<TaskResponseDTO> result = taskService.getMyTasks(pageable);
        return ResponseEntity.ok(ApiResponse.success("Tasks fetched successfully",result));
    }

    @GetMapping("/overdue")
    public ResponseEntity<ApiResponse<List<TaskResponseDTO>>> getOverdueTasks(){
        List<TaskResponseDTO> result = taskService.getOverdueTasks();
        return ResponseEntity.ok(ApiResponse.success("Tasks fetched successfully",result));
    }

    @GetMapping("/epics")
    public ResponseEntity<ApiResponse<List<TaskResponseDTO>>> getAllEpics(){
        List<TaskResponseDTO> result = taskService.getAllEpics();
        return ResponseEntity.ok(ApiResponse.success("Tasks fetched successfully",result));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<ApiResponse<Page<TaskResponseDTO>>> getTasksByProject(@PathVariable Long projectId, Pageable pageable){
        Page<TaskResponseDTO> result = taskService.getTasksByProject(projectId,pageable);
        return ResponseEntity.ok(ApiResponse.success("Tasks fetched successfully",result));
    }

    @GetMapping("/assignee/{userId}")
    public ResponseEntity<ApiResponse<Page<TaskResponseDTO>>> getTasksByAssignee(@PathVariable Long userId, Pageable pageable){
        Page<TaskResponseDTO> result = taskService.getTasksByAssignee(userId,pageable);
        return ResponseEntity.ok(ApiResponse.success("Tasks fetched successfully",result));
    }

    @GetMapping("/creator/{userId}")
    public ResponseEntity<ApiResponse<Page<TaskResponseDTO>>> getTasksByCreator(@PathVariable Long userId, Pageable pageable){
        Page<TaskResponseDTO> result = taskService.getTasksByCreator(userId,pageable);
        return ResponseEntity.ok(ApiResponse.success("Tasks fetched successfully", result));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<Page<TaskResponseDTO>>> getTasksByStatus(@PathVariable TaskStatus status, Pageable pageable){
        Page<TaskResponseDTO> result = taskService.getTasksByStatus(status,pageable);
        return ResponseEntity.ok(ApiResponse.success("Tasks fetched successfully", result));
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<ApiResponse<Page<TaskResponseDTO>>> getTasksByPriority(@PathVariable TaskPriority priority, Pageable pageable){
        Page<TaskResponseDTO> result = taskService.getTasksByPriority(priority,pageable);
        return ResponseEntity.ok(ApiResponse.success("Tasks fetched successfully", result));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponse<Page<TaskResponseDTO>>> getTasksByType(@PathVariable TaskType type, Pageable pageable){
        Page<TaskResponseDTO> result = taskService.getTasksByType(type, pageable);
        return ResponseEntity.ok(ApiResponse.success("Tasks fetched successfully", result));
    }

    @GetMapping("/project/{projectId}/priority/{priority}")
    public ResponseEntity<ApiResponse<Page<TaskResponseDTO>>> getTasksByProjectAndPriority(@PathVariable Long projectId,@PathVariable TaskPriority priority, Pageable pageable){
        Page<TaskResponseDTO> result = taskService.getTasksByProjectAndPriority(projectId,priority,pageable);
        return ResponseEntity.ok(ApiResponse.success("Tasks fetched successfully", result));
    }

    @GetMapping("/project/{projectId}/status/{status}")
    public ResponseEntity<ApiResponse<Page<TaskResponseDTO>>> getTasksByProjectAndStatus(@PathVariable Long projectId,@PathVariable TaskStatus status, Pageable pageable){
        Page<TaskResponseDTO> result = taskService.getTasksByProjectAndStatus(projectId,status,pageable);
        return ResponseEntity.ok(ApiResponse.success("Tasks fetched successfully", result));
    }

    @GetMapping("/assignee/{userId}/status/{status}")
    public ResponseEntity<ApiResponse<Page<TaskResponseDTO>>> getTasksByAssigneeAndStatus(@PathVariable Long userId, @PathVariable TaskStatus status, Pageable pageable) {
        Page<TaskResponseDTO> result = taskService.getTasksByAssigneeAndStatus(userId, status, pageable);
        return ResponseEntity.ok(ApiResponse.success("Tasks fetched successfully", result));
    }

    @GetMapping("/search/title")
    public ResponseEntity<ApiResponse<Page<TaskResponseDTO>>> searchTasksByTitle(@RequestParam String query, Pageable pageable){
        Page<TaskResponseDTO> result = taskService.searchTasksByTitle(query, pageable);
        return ResponseEntity.ok(ApiResponse.success("Tasks fetched successfully", result));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<TaskResponseDTO>>> searchTasks(@RequestParam String query, Pageable pageable){
        Page<TaskResponseDTO> result = taskService.searchTasks(query,pageable);
        return ResponseEntity.ok(ApiResponse.success("Tasks fetched successfully", result));
    }

    @GetMapping("/{id}/children")
    public ResponseEntity<ApiResponse<List<TaskResponseDTO>>> getChildTasks(@PathVariable Long id){
        List<TaskResponseDTO> result = taskService.getChildTasks(id);
        return ResponseEntity.ok(ApiResponse.success("Children fetched successfully", result));
    }

    @GetMapping("/project/{projectId}/unassigned")
    public ResponseEntity<ApiResponse<List<TaskResponseDTO>>> getUnassignedTasks(@PathVariable Long projectId){
        List<TaskResponseDTO> result = taskService.getUnassignedTasks(projectId);
        return ResponseEntity.ok(ApiResponse.success("Tasks unassigned successfully", result));
    }

    @PatchMapping("/{taskId}/assign")
    public ResponseEntity<ApiResponse<TaskResponseDTO>> assignTask(@PathVariable Long taskId, @RequestParam Long userId){
        TaskResponseDTO result = taskService.assignTask(taskId,userId);
        return ResponseEntity.ok(ApiResponse.success("Task assigned successfully", result));
    }

    @PatchMapping("/{taskId}/unassign")
    public ResponseEntity<ApiResponse<TaskResponseDTO>> unassignTask(@PathVariable Long taskId){
        TaskResponseDTO result = taskService.unassignTask(taskId);
        return ResponseEntity.ok(ApiResponse.success("task unassigned successfully", result));
    }
}
