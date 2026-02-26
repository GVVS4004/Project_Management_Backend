package com.fullstack.backend.service;

import com.fullstack.backend.dto.request.CreateTaskDTO;
import com.fullstack.backend.dto.request.UpdateTaskDTO;
import com.fullstack.backend.dto.response.TaskResponseDTO;
import com.fullstack.backend.dto.response.UserSummaryDTO;
import com.fullstack.backend.entity.*;
import com.fullstack.backend.exception.BadRequestException;
import com.fullstack.backend.exception.ResourceNotFoundException;
import com.fullstack.backend.exception.ForbiddenException;
import com.fullstack.backend.exception.UnauthorizedException;
import com.fullstack.backend.repository.ProjectMemberRepository;
import com.fullstack.backend.repository.ProjectRepository;
import com.fullstack.backend.repository.TaskRepository;
import com.fullstack.backend.repository.UserRepository;
import com.fullstack.backend.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor

public class TaskServiceImpl  implements TaskService{
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMemberRepository projectMemberRepository;



    private User getCurrentUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null || !auth.isAuthenticated()){
            throw new UnauthorizedException("User not authenticated");
        }
        Object principal = auth.getPrincipal();
        if (!(principal instanceof CustomUserDetails)) {
            throw new UnauthorizedException("Invalid authentication principal");
        }
        CustomUserDetails user = (CustomUserDetails) principal;
        return user.getUser();
    }

    private UserSummaryDTO convertUserToSummary(User user){
        if(user == null){
            return null;
        }
        UserSummaryDTO dto = new UserSummaryDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUserName());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setProfileImageUrl(user.getProfileImageUrl());

        return dto;
    }

    private TaskResponseDTO convertToDTO(Task task){
        TaskResponseDTO dto = new TaskResponseDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setPriority(task.getPriority());
        dto.setType(task.getType());
        dto.setDueDate(task.getDueDate());
        dto.setCompletedAt(task.getCompletedAt());
        dto.setCreatedAt(task.getCreatedAt());
        dto.setUpdatedAt(task.getUpdatedAt());

        dto.setProjectId(task.getProject().getId());
        dto.setProjectName(task.getProject().getName());

        dto.setCreatedBy(convertUserToSummary(task.getCreatedBy()));
        dto.setAssignedTo(convertUserToSummary(task.getAssignedTo()));

        // Parent task information (optional - can be null)
        if (task.getParentTask() != null) {
            dto.setParentTaskId(task.getParentTask().getId());
            dto.setParentTaskTitle(task.getParentTask().getTitle());
        }

        return dto;
    }

    private void validateTaskHierarchy(TaskType type, Long parentTaskId, Task parentTask){

        if(type == TaskType.SUBTASK && parentTaskId== null){
            throw new BadRequestException("SUBTASK must have a parent task");
        }

        if(parentTask != null){
            if(type == TaskType.SUBTASK && parentTask.getType() == TaskType.SUBTASK){
                throw new BadRequestException("SUBTASK cannot be a child of another SUBTASK");
            }
            if(parentTask.getType() == TaskType.EPIC){
                if(type == TaskType.SUBTASK || type == TaskType.EPIC){
                    throw new BadRequestException("EPIC can only have STORY, TASK, BUG, or DEFECT as children");
                }
            }
            if(parentTask.getType() == TaskType.STORY||
               parentTask.getType() == TaskType.TASK||
               parentTask.getType() == TaskType.BUG||
               parentTask.getType() == TaskType.DEFECT
            ){
                if (type != TaskType.SUBTASK) {
                    throw new BadRequestException(
                            parentTask.getType() + " can only have SUBTASK as children"
                    );
                }
            }
        }
        if (type == TaskType.EPIC && parentTaskId != null) {
            throw new BadRequestException("EPIC cannot have a parent task");
        }
        if (type == TaskType.DEFECT && (parentTaskId == null || parentTask.getType() != TaskType.STORY)) {
            throw new BadRequestException("DEFECT must have a STORY as parent");
        }
    }

    private void validateStatusTransition(TaskStatus currentStatus, TaskStatus newStatus){
        if(currentStatus == TaskStatus.DONE || currentStatus == TaskStatus.ABANDONED){
            throw new BadRequestException(
                    "Cannot change status from" + currentStatus + "(final state)"
            );
        }
        if(currentStatus == newStatus){
            return;
        }

        boolean isValidTransition = switch (currentStatus) {
            case TODO -> newStatus == TaskStatus.IN_PROGRESS || newStatus == TaskStatus.ABANDONED;

            case IN_PROGRESS -> newStatus == TaskStatus.BLOCKED ||
                                newStatus == TaskStatus.REVIEW ||
                                newStatus == TaskStatus.ABANDONED;

            case BLOCKED -> newStatus == TaskStatus.IN_PROGRESS ||
                            newStatus == TaskStatus.ABANDONED;

            case REVIEW -> newStatus == TaskStatus.IN_PROGRESS ||
                           newStatus == TaskStatus.TESTING;

            case TESTING -> newStatus == TaskStatus.IN_PROGRESS ||
                            newStatus == TaskStatus.DONE;

            default -> false;
        };
        if (!isValidTransition) {
            throw new BadRequestException(
                    "Invalid status transition from " + currentStatus + " to " + newStatus
            );
        }
    }

    private void checkProjectMembership(Long projectId, User user) {
        // Project owner always has access
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));
        if (project.getOwner().getId().equals(user.getId())) {
            return;
        }

        boolean isMember = projectMemberRepository.existsByProjectIdAndUserId(projectId, user.getId());
        if (!isMember) {
            throw new ForbiddenException("You are not a member of this project");
        }
    }

    private void checkProjectOwnership(Long projectId, User user){
        ProjectMember projectMember  = projectMemberRepository.findByProjectIdAndUserId(projectId,user.getId()).orElseThrow(()-> new ResourceNotFoundException("User does not exist in the project"));

        Project project = projectRepository.findById(projectId).orElseThrow(()-> new ResourceNotFoundException("Project not found with id:" + projectId));
        if(!projectMember.getRole().equals(ProjectRole.ADMIN) && !project.getOwner().equals(user)){
            throw new ForbiddenException("User is not an Owner or Admin of this project");
        }
    }

    private void checkDeleteAuthorization(Task task, User currentUser){
        if(task.getCreatedBy().equals(currentUser)) {
            return;
        }
        if(currentUser.getRole().equals(Role.ADMIN)){
            return;
        }
        checkProjectMembership(task.getProject().getId(), currentUser);
        checkProjectOwnership(task.getProject().getId(),currentUser);
    }

    @Override
    @Transactional
    public TaskResponseDTO createTask(CreateTaskDTO dto) {
        User currentUser = getCurrentUser();

        Project project = projectRepository.findById(dto.getProjectId()).orElseThrow(()-> new ResourceNotFoundException("Project not found with id: " + dto.getProjectId()));

        checkProjectMembership(dto.getProjectId(), currentUser);

        User assignedUser = null;
        if(dto.getAssignedToId() != null){
            assignedUser = userRepository.findById(dto.getAssignedToId())
                    .orElseThrow(()-> new ResourceNotFoundException(
                            "User not found with id: " + dto.getAssignedToId()
                    ));
        }

        Task parentTask = null;
        if(dto.getParentTaskId() != null){
            parentTask = taskRepository.findById(dto.getParentTaskId())
                    .orElseThrow(()-> new ResourceNotFoundException(
                            "Parent task not found with id: " + dto.getParentTaskId()
                    ));

            if(!parentTask.getProject().getId().equals(dto.getProjectId())){
                throw new BadRequestException(
                        "Parent task must belong to the same project"
                );
            }
        }

        TaskType type = dto.getType() != null ? dto.getType() : TaskType.TASK;
        validateTaskHierarchy(type, dto.getParentTaskId(), parentTask);

        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setType(type);
        task.setPriority(dto.getPriority() != null ? dto.getPriority() : TaskPriority.MEDIUM);
        task.setStatus(TaskStatus.TODO);
        task.setProject(project);
        task.setCreatedBy(currentUser);
        task.setAssignedTo(assignedUser);
        task.setParentTask(parentTask);
        task.setDueDate(dto.getDueDate());

        Task savedTask = taskRepository.save(task);

        return convertToDTO(savedTask);
    }

    @Override
    public TaskResponseDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Task not found with id:" + id));
        return convertToDTO(task);
    }

    @Override
    @Transactional
    public TaskResponseDTO updateTask(Long id, UpdateTaskDTO dto) {
        Task task = taskRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Task not found with id:" + id));

        User currentUser = getCurrentUser();

        checkProjectMembership(task.getProject().getId(),currentUser);

        if(dto.getAssignedToId() != null){
            User assignedTo = userRepository.findById(dto.getAssignedToId()).orElseThrow(()-> new ResourceNotFoundException("User not found with id: "+ dto.getAssignedToId()));
            task.setAssignedTo(assignedTo);
        }

        if(dto.getTitle() != null && !dto.getTitle().trim().isEmpty()){
            task.setTitle(dto.getTitle());
        }

        if(dto.getDescription() != null){
            task.setDescription(dto.getDescription());
        }

        if(dto.getPriority() != null){
            task.setPriority(dto.getPriority());
        }

        if(dto.getType() != null){
            task.setType(dto.getType());
        }

        if(dto.getDueDate() !=null){
            task.setDueDate(dto.getDueDate());
        }

        Task savedTask = taskRepository.save(task);
        return convertToDTO(savedTask);
    }

    @Override
    @Transactional
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Task not found with id:" + id));

        User currentUser = getCurrentUser();

       checkDeleteAuthorization(task,currentUser);

       List<Task> childTasks = taskRepository.findByParentTaskId(task.getId());

       if(!childTasks.isEmpty()){
           throw new BadRequestException("Cannot delete task with " + childTasks.size() + " subtask(s). Please delete subtasks first.");
       }

       taskRepository.deleteById(task.getId());
    }

    @Override
    @Transactional
    public TaskResponseDTO updateTaskStatus(Long id, TaskStatus newStatus) {
        Task task = taskRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Task not found with id:" + id));
        validateStatusTransition(task.getStatus(),newStatus);
        User currentUser = getCurrentUser();
        checkProjectMembership(task.getProject().getId(),currentUser);
        task.setStatus(newStatus);
        if(newStatus==TaskStatus.DONE){
            task.setCompletedAt(LocalDateTime.now());
        }
        taskRepository.save(task);
        return convertToDTO(task);
    }

    @Override
    public Page<TaskResponseDTO> getTasksByProject(Long projectId, Pageable pageable) {
        Page<Task> tasks = taskRepository.findByProjectId(projectId,pageable);
        return tasks.map(this::convertToDTO);
    }

    @Override
    public Page<TaskResponseDTO> getTasksByAssignee(Long userId, Pageable pageable) {
        Page<Task> tasks = taskRepository.findByAssignedToId(userId,pageable);
        return tasks.map(this::convertToDTO);
    }

    @Override
    public Page<TaskResponseDTO> getTasksByCreator(Long userId, Pageable pageable) {
        Page<Task> tasks = taskRepository.findByCreatedById(userId,pageable);
        return tasks.map(this::convertToDTO);
    }

    @Override
    public Page<TaskResponseDTO> getMyTasks(Pageable pageable) {
        User currentUser = getCurrentUser();
        Page<Task> tasks = taskRepository.findByAssignedToId(currentUser.getId(),pageable);
        return tasks.map(this::convertToDTO);
    }

    @Override
    public List<TaskResponseDTO> getChildTasks(Long parentTaskId) {
        List<Task> tasks = taskRepository.findByParentTaskId(parentTaskId);
        return tasks.stream().map(this::convertToDTO).toList();
    }

    @Override
    public Page<TaskResponseDTO> getTasksByStatus(TaskStatus status, Pageable pageable) {
        Page<Task> tasks = taskRepository.findByStatus(status, pageable);

        return tasks.map(this::convertToDTO);
    }

    @Override
    public Page<TaskResponseDTO> getTasksByPriority(TaskPriority priority, Pageable pageable) {
        Page<Task> tasks = taskRepository.findByPriority(priority,pageable);

        return tasks.map(this::convertToDTO);
    }

    @Override
    public Page<TaskResponseDTO> getTasksByType(TaskType type, Pageable pageable) {
        Page<Task> tasks = taskRepository.findByType(type,pageable);

        return tasks.map(this::convertToDTO);
    }

    @Override
    public Page<TaskResponseDTO> getTasksByProjectAndStatus(Long projectId, TaskStatus status, Pageable pageable) {
        Page<Task> tasks = taskRepository.findByProjectIdAndStatus(projectId,status,pageable);

        return tasks.map(this::convertToDTO);
    }

    @Override
    public Page<TaskResponseDTO> getTasksByProjectAndPriority(Long projectId, TaskPriority priority, Pageable pageable) {
        Page<Task> tasks = taskRepository.findByProjectIdAndPriority(projectId,priority,pageable);

        return tasks.map(this::convertToDTO);
    }

    @Override
    public Page<TaskResponseDTO> getTasksByAssigneeAndStatus(Long userId, TaskStatus status, Pageable pageable) {
        Page<Task> tasks = taskRepository.findByAssignedToIdAndStatus(userId,status,pageable);

        return tasks.map(this::convertToDTO);
    }

    @Override
    public Page<TaskResponseDTO> searchTasksByTitle(String title, Pageable pageable) {
        Page<Task> tasks = taskRepository.findByTitleContainingIgnoreCase(title,pageable);

        return tasks.map(this::convertToDTO);
    }

    @Override
    public Page<TaskResponseDTO> searchTasks(String searchTerm, Pageable pageable) {
        Page<Task> tasks = taskRepository.searchByTitleOrDescription(searchTerm,pageable);

        return tasks.map(this::convertToDTO);
    }

    @Override
    public List<TaskResponseDTO> getOverdueTasks() {
        LocalDate today = LocalDate.now();
        List<Task> tasks = taskRepository.findOverdueTasks(today);

        return tasks.stream().map(this::convertToDTO).toList();
    }

    @Override
    public List<TaskResponseDTO> getAllEpics() {
        List<Task> tasks = taskRepository.findAllEpics();

        return tasks.stream().map(this::convertToDTO).toList();
    }

    @Override
    public List<TaskResponseDTO> getUnassignedTasks(Long projectId) {
        List<Task> tasks = taskRepository.findByProjectIdAndAssignedToIdIsNull(projectId);

        return tasks.stream().map(this::convertToDTO).toList();
    }

    @Override
    @Transactional
    public TaskResponseDTO assignTask(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId).orElseThrow(()-> new ResourceNotFoundException("Task not found with id:" + taskId));
        User currentUser = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found with id:" + userId));

        task.setAssignedTo(currentUser);
        Task savedTask = taskRepository.save(task);
        return convertToDTO(savedTask);
    }

    @Override
    @Transactional
    public TaskResponseDTO unassignTask(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(()-> new ResourceNotFoundException("Task not found with id:" + taskId));

        task.setAssignedTo(null);
        Task savedTask = taskRepository.save(task);
        return convertToDTO(savedTask);
    }
}
