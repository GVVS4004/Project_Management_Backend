package com.fullstack.backend.service;


import com.fullstack.backend.dto.response.DailyCountDTO;
import com.fullstack.backend.dto.response.MyStatsDTO;
import com.fullstack.backend.dto.response.ProjectStatsDTO;
import com.fullstack.backend.dto.response.TrendDataDTO;
import com.fullstack.backend.entity.*;
import com.fullstack.backend.repository.ProjectMemberRepository;
import com.fullstack.backend.repository.ProjectRepository;
import com.fullstack.backend.repository.TaskRepository;
import com.fullstack.backend.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService{

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    private <E extends Enum<E>> Map<String, Long> initializeMap(E[] values) {
        Map<String, Long> map = new LinkedHashMap<>();
        for (E value : values) {
            map.put(value.name(), 0L);
        }
        return map;
    }

    @Override
    public MyStatsDTO getMyStats() {
        User currentUser = SecurityUtils.getCurrentUser();
        Long userId = currentUser.getId();

        Map<String, Long> tasksByStatus = initializeMap(TaskStatus.values());
        for (Object[] row : taskRepository.countByAssignedUserGroupedByStatus(userId)) {
            tasksByStatus.put(row[0].toString(), (Long) row[1]);
        }

        Map<String, Long> tasksByPriority = initializeMap(TaskPriority.values());
        for (Object[] row : taskRepository.countByAssignedUserGroupedByPriority(userId)) {
            tasksByPriority.put(row[0].toString(), (Long) row[1]);
        }
        // Total active assigned tasks (exclude DONE and ABANDONED)
        long totalAssigned = tasksByStatus.entrySet().stream()
                .filter(e -> !e.getKey().equals("DONE") && !e.getKey().equals("ABANDONED"))
                .mapToLong(Map.Entry::getValue)
                .sum();

        long overdueCount = taskRepository.countOverdueByAssignedToId(userId, LocalDate.now());

        List<Long> projectIds = projectRepository.findProjectIdsByUserId(userId);
        Map<String, Long> projectsByStatus = initializeMap(ProjectStatus.values());
        for (Long projectId : projectIds) {
            Project project = projectRepository.findById(projectId).orElse(null);
            if (project != null) {
                String status = project.getStatus().toString();
                projectsByStatus.merge(status, 1L, Long::sum);
            }
        }

        MyStatsDTO dto = new MyStatsDTO();
        dto.setTasksByStatus(tasksByStatus);
        dto.setTasksByPriority(tasksByPriority);
        dto.setTotalAssignedTasks(totalAssigned);
        dto.setOverdueTaskCount(overdueCount);
        dto.setProjectsByStatus(projectsByStatus);
        dto.setTotalProjects(projectIds.size());
        return dto;
    }

    @Override
    public List<ProjectStatsDTO> getProjectStats() {
        User currentUser = SecurityUtils.getCurrentUser();
        List<Long> projectIds = projectRepository.findProjectIdsByUserId(currentUser.getId());

        List<ProjectStatsDTO> result = new ArrayList<>();

        for (Long projectId : projectIds) {
            Project project = projectRepository.findById(projectId).orElse(null);
            if (project == null) continue;

            long totalTasks = taskRepository.countByProjectId(projectId);
            long completedTasks = taskRepository.countByProjectIdAndStatus(projectId, TaskStatus.DONE);
            long overdueTasks = taskRepository.countOverdueByProjectId(projectId, LocalDate.now());
            long memberCount = projectMemberRepository.countByProjectId(projectId) + 1; // +1 for owner

            ProjectStatsDTO dto = new ProjectStatsDTO();
            dto.setProjectId(projectId);
            dto.setProjectName(project.getName());
            dto.setProjectStatus(project.getStatus().toString());
            dto.setTotalTasks(totalTasks);
            dto.setCompletedTasks(completedTasks);
            dto.setCompletionPercentage(totalTasks > 0 ? Math.round((double) completedTasks / totalTasks *
                    100 * 10) / 10.0 : 0.0);
            dto.setOverdueTaskCount(overdueTasks);
            dto.setMemberCount(memberCount);

            result.add(dto);
        }
        return result;
    }
    @Override
    public TrendDataDTO getTrends(int days) {
        if (days < 1) days = 30;
        if (days > 90) days = 90;

        User currentUser = SecurityUtils.getCurrentUser();
        List<Long> projectIds = projectRepository.findProjectIdsByUserId(currentUser.getId());

        LocalDateTime since = LocalDate.now().minusDays(days).atStartOfDay();

        // Get raw data from DB
        Map<LocalDate, Long> completedMap = new HashMap<>();
        Map<LocalDate, Long> createdMap = new HashMap<>();

        if (!projectIds.isEmpty()) {
            for (Object[] row : taskRepository.countCompletedPerDay(projectIds, since)) {
                completedMap.put((LocalDate) row[0], (Long) row[1]);
            }
            for (Object[] row : taskRepository.countCreatedPerDay(projectIds, since)) {
                createdMap.put((LocalDate) row[0], (Long) row[1]);
            }
        }

        // Fill in all days (including zero-count days)
        List<DailyCountDTO> completed = new ArrayList<>();
        List<DailyCountDTO> created = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (int i = days; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            completed.add(new DailyCountDTO(date, completedMap.getOrDefault(date, 0L)));
            created.add(new DailyCountDTO(date, createdMap.getOrDefault(date, 0L)));
        }

        TrendDataDTO dto = new TrendDataDTO();
        dto.setPeriod(days);
        dto.setTasksCompleted(completed);
        dto.setTasksCreated(created);
        return dto;
    }
}
