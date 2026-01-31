package com.fullstack.backend.controller;

import com.fullstack.backend.dto.request.AddMemberDTO;
import com.fullstack.backend.dto.request.CreateProjectDTO;
import com.fullstack.backend.dto.request.UpdateMemberRoleDTO;
import com.fullstack.backend.dto.request.UpdateProjectDTO;
import com.fullstack.backend.dto.response.ApiResponse;
import com.fullstack.backend.dto.response.MemberResponseDTO;
import com.fullstack.backend.dto.response.ProjectResponseDTO;
import com.fullstack.backend.entity.ProjectStatus;
import com.fullstack.backend.service.IProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final IProjectService projectService;

    /**
     * Create a new project
     * POST /api/projects
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ProjectResponseDTO>> createProject(
            @Valid @RequestBody CreateProjectDTO dto) {
        ProjectResponseDTO created = projectService.createProject(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Project created successfully", created));
    }

    /**
     * Get project by ID
     * GET /api/projects/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectResponseDTO>> getProjectById(
            @PathVariable Long id) {
        ProjectResponseDTO project = projectService.getProjectById(id);
        return ResponseEntity.ok(
                ApiResponse.success("Project retrieved successfully", project));
    }

    /**
     * Get all projects with pagination
     * GET /api/projects?page=0&size=20&sort=createdAt,desc
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProjectResponseDTO>>> getAllProjects(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<ProjectResponseDTO> projects = projectService.getAllProjects(pageable);
        return ResponseEntity.ok(
                ApiResponse.success("Projects retrieved successfully", projects));
    }

    /**
     * Update project details
     * PUT /api/projects/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectResponseDTO>> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProjectDTO dto) {
        ProjectResponseDTO updated = projectService.updateProject(id, dto);
        return ResponseEntity.ok(
                ApiResponse.success("Project updated successfully", updated));
    }

    /**
     * Delete project
     * DELETE /api/projects/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok(
                ApiResponse.success("Project deleted successfully", null));
    }

    /**
     * Update project status
     * PATCH /api/projects/{id}/status?status=ACTIVE
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<ProjectResponseDTO>> updateProjectStatus(
            @PathVariable Long id,
            @RequestParam ProjectStatus status) {
        ProjectResponseDTO updated = projectService.updateProjectStatus(id, status);
        return ResponseEntity.ok(
                ApiResponse.success("Project status updated successfully", updated));
    }

    /**
     * Search projects by name
     * GET /api/projects/search/name?query=project&page=0&size=20
     */
    @GetMapping("/search/name")
    public ResponseEntity<ApiResponse<Page<ProjectResponseDTO>>> searchByName(
            @RequestParam String query,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<ProjectResponseDTO> projects = projectService.searchProjectsByName(query, pageable);
        return ResponseEntity.ok(
                ApiResponse.success("Projects found", projects));
    }

    /**
     * Search projects by status
     * GET /api/projects/search/status?status=ACTIVE&page=0&size=20
     */
    @GetMapping("/search/status")
    public ResponseEntity<ApiResponse<Page<ProjectResponseDTO>>> searchByStatus(
            @RequestParam ProjectStatus status,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<ProjectResponseDTO> projects = projectService.searchProjectsByStatus(status, pageable);
        return ResponseEntity.ok(
                ApiResponse.success("Projects found", projects));
    }

    /**
     * Get current user's projects
     * GET /api/projects/my?page=0&size=20
     */
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<Page<ProjectResponseDTO>>> getMyProjects(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<ProjectResponseDTO> projects = projectService.getMyProjects(pageable);
        return ResponseEntity.ok(
                ApiResponse.success("Your projects retrieved successfully", projects));
    }

    // ==================== Member Management Endpoints ====================

    /**
     * Get all members of a project
     * GET /api/projects/{projectId}/members
     */
    @GetMapping("/{projectId}/members")
    public ResponseEntity<ApiResponse<List<MemberResponseDTO>>> getProjectMembers(
            @PathVariable Long projectId) {
        List<MemberResponseDTO> members = projectService.getProjectMembers(projectId);
        return ResponseEntity.ok(
                ApiResponse.success("Project members retrieved successfully", members));
    }

    /**
     * Add a member to a project
     * POST /api/projects/{projectId}/members
     */
    @PostMapping("/{projectId}/members")
    public ResponseEntity<ApiResponse<MemberResponseDTO>> addMember(
            @PathVariable Long projectId,
            @Valid @RequestBody AddMemberDTO dto) {
        MemberResponseDTO member = projectService.addMember(projectId, dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Member added successfully", member));
    }

    /**
     * Remove a member from a project
     * DELETE /api/projects/{projectId}/members/{userId}
     */
    @DeleteMapping("/{projectId}/members/{userId}")
    public ResponseEntity<ApiResponse<Void>> removeMember(
            @PathVariable Long projectId,
            @PathVariable Long userId) {
        projectService.removeMember(projectId, userId);
        return ResponseEntity.ok(
                ApiResponse.success("Member removed successfully", null));
    }

    /**
     * Update a member's role in a project
     * PATCH /api/projects/{projectId}/members/{userId}/role
     */
    @PatchMapping("/{projectId}/members/{userId}/role")
    public ResponseEntity<ApiResponse<MemberResponseDTO>> updateMemberRole(
            @PathVariable Long projectId,
            @PathVariable Long userId,
            @Valid @RequestBody UpdateMemberRoleDTO dto) {
        MemberResponseDTO member = projectService.updateMemberRole(projectId, userId, dto);
        return ResponseEntity.ok(
                ApiResponse.success("Member role updated successfully", member));
    }
}