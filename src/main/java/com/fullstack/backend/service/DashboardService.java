package com.fullstack.backend.service;

import com.fullstack.backend.dto.response.MyStatsDTO;
import com.fullstack.backend.dto.response.ProjectStatsDTO;
import com.fullstack.backend.dto.response.TrendDataDTO;

import java.util.List;

public interface DashboardService {
    MyStatsDTO getMyStats();
    List<ProjectStatsDTO> getProjectStats();
    TrendDataDTO getTrends(int days);
}
