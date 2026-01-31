package com.fullstack.backend.repository;

import com.fullstack.backend.entity.Task;
import com.fullstack.backend.entity.TaskPriority;
import com.fullstack.backend.entity.TaskStatus;
import com.fullstack.backend.entity.TaskType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // ========== FIND BY SINGLE FIELD (with pagination) ==========

    // Find all tasks in a project
    Page<Task> findByProjectId(Long projectId, Pageable pageable);

    // Find all tasks assigned to a user
    Page<Task> findByAssignedToId(Long userId, Pageable pageable);

    // Find all tasks created by a user
    Page<Task> findByCreatedById(Long userId, Pageable pageable);

    // Find all tasks by status
    Page<Task> findByStatus(TaskStatus status, Pageable pageable);

    // Find all tasks by priority
    Page<Task> findByPriority(TaskPriority priority, Pageable pageable);

    // Find all tasks by type
    Page<Task> findByType(TaskType type, Pageable pageable);

    // Find all child tasks (subtasks) of a parent
    List<Task> findByParentTaskId(Long parentTaskId);


    // ========== COMBINED QUERIES (project + filters) ==========

    // Find tasks in a project with specific status
    Page<Task> findByProjectIdAndStatus(Long projectId, TaskStatus status, Pageable pageable);

    // Find tasks in a project assigned to a user
    Page<Task> findByProjectIdAndAssignedToId(Long projectId, Long userId, Pageable pageable);

    // Find tasks in a project with specific priority
    Page<Task> findByProjectIdAndPriority(Long projectId, TaskPriority priority, Pageable pageable);

    // Find tasks in a project by type
    Page<Task> findByProjectIdAndType(Long projectId, TaskType type, Pageable pageable);


    // ========== COMBINED QUERIES (assignee + filters) ==========

    // Find assigned tasks with specific status
    Page<Task> findByAssignedToIdAndStatus(Long userId, TaskStatus status, Pageable pageable);

    // Find assigned tasks with specific priority
    Page<Task> findByAssignedToIdAndPriority(Long userId, TaskPriority priority, Pageable pageable);


    // ========== SEARCH QUERIES ==========

    // Search tasks by title (case-insensitive, partial match)
    Page<Task> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    // Search tasks by title in a specific project
    Page<Task> findByProjectIdAndTitleContainingIgnoreCase(Long projectId, String title, Pageable pageable);

    // Search tasks by title or description (custom JPQL query)
    @Query("SELECT t FROM Task t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(t.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Task> searchByTitleOrDescription(@Param("searchTerm") String searchTerm, Pageable pageable);


    // ========== DATE-BASED QUERIES ==========

    // Find tasks with due date before a certain date (overdue tasks)
    @Query("SELECT t FROM Task t WHERE t.dueDate < :date AND t.status NOT IN ('DONE', 'ABANDONED')")
    List<Task> findOverdueTasks(@Param("date") LocalDate date);

    // Find tasks due in a project before a certain date
    List<Task> findByProjectIdAndDueDateBefore(Long projectId, LocalDate date);


    // ========== EXISTENCE CHECKS ==========

    // Check if a task exists by ID
    boolean existsById(Long id);

    // Check if a project has any tasks
    boolean existsByProjectId(Long projectId);


    // ========== COUNTING ==========

    // Count tasks in a project
    long countByProjectId(Long projectId);

    // Count tasks in a project by status
    long countByProjectIdAndStatus(Long projectId, TaskStatus status);

    // Count tasks assigned to a user
    long countByAssignedToId(Long userId);


    // ========== SPECIALIZED QUERIES ==========

    // Find all Epic-level tasks (no parent)
    @Query("SELECT t FROM Task t WHERE t.type = 'EPIC' AND t.parentTask IS NULL")
    List<Task> findAllEpics();

    // Find all tasks without a parent (standalone tasks + epics)
    List<Task> findByParentTaskIdIsNull();

    // Find all unassigned tasks in a project
    List<Task> findByProjectIdAndAssignedToIdIsNull(Long projectId);
}
