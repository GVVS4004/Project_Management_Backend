package com.fullstack.backend.dto.response;

import lombok.Data;

@Data
public class ProjectStatsDTO {
    private Long projectId;
    private String projectName;
    private String projectStatus;
    private long totalTasks;
    private long completedTasks;
    private double completionPercentage;
    private long overdueTaskCount;
    private long memberCount;
}
