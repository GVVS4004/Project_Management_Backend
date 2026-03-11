package com.fullstack.backend.dto.response;

import lombok.Data;

import java.util.Map;

@Data
public class MyStatsDTO {
    private Map<String, Long> tasksByStatus;
    private Map<String, Long> tasksByPriority;
    private long totalAssignedTasks;
    private long overdueTaskCount;
    private Map<String, Long> projectsByStatus;
    private long totalProjects;
}
