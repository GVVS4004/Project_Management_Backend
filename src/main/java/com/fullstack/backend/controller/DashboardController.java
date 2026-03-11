package com.fullstack.backend.controller;

import com.fullstack.backend.dto.response.ApiResponse;
import com.fullstack.backend.dto.response.MyStatsDTO;
import com.fullstack.backend.dto.response.ProjectStatsDTO;
import com.fullstack.backend.dto.response.TrendDataDTO;
import com.fullstack.backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/my-stats")
    public ResponseEntity<ApiResponse<MyStatsDTO>> getMyStats() {
        MyStatsDTO result = dashboardService.getMyStats();
        return ResponseEntity.ok(ApiResponse.success("Stats fetched successfully", result));
    }

    @GetMapping("/project-stats")
    public ResponseEntity<ApiResponse<List<ProjectStatsDTO>>> getProjectStats() {
        List<ProjectStatsDTO> result = dashboardService.getProjectStats();
        return ResponseEntity.ok(ApiResponse.success("Project stats fetched successfully", result));
    }

    @GetMapping("/trends")
    public ResponseEntity<ApiResponse<TrendDataDTO>> getTrends(@RequestParam(defaultValue = "30") int days) {
        TrendDataDTO result = dashboardService.getTrends(days);
        return ResponseEntity.ok(ApiResponse.success("Trends fetched successfully", result));
    }
}
