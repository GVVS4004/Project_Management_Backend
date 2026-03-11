package com.fullstack.backend.service;

import com.fullstack.backend.dto.request.AddMemberDTO;
import com.fullstack.backend.dto.request.CreateProjectDTO;
import com.fullstack.backend.dto.request.UpdateMemberRoleDTO;
import com.fullstack.backend.dto.request.UpdateProjectDTO;
import com.fullstack.backend.dto.response.MemberResponseDTO;
import com.fullstack.backend.dto.response.ProjectResponseDTO;
import com.fullstack.backend.entity.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface IProjectService {
    // Project management methods
    ProjectResponseDTO createProject(CreateProjectDTO createProjectDTO);
    ProjectResponseDTO getProjectById(Long id);
    Page<ProjectResponseDTO> getAllProjects(String search, ProjectStatus status, Long ownerId,Pageable pageable);
    ProjectResponseDTO updateProject(Long id, UpdateProjectDTO dto);
    void deleteProject(Long id);
    ProjectResponseDTO updateProjectStatus(Long id, ProjectStatus status);
    Page<ProjectResponseDTO> searchProjectsByName(String name, Pageable pageable);
    Page<ProjectResponseDTO> searchProjectsByStatus(ProjectStatus status, Pageable pageable);
    Page<ProjectResponseDTO> getMyProjects(Pageable pageable);

    // Member management methods
    MemberResponseDTO addMember(Long projectId, AddMemberDTO dto);
    void removeMember(Long projectId, Long userId);
    MemberResponseDTO updateMemberRole(Long projectId, Long userId, UpdateMemberRoleDTO dto);
    List<MemberResponseDTO> getProjectMembers(Long projectId);
}
