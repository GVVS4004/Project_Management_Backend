package com.fullstack.backend.service;

import com.fullstack.backend.dto.request.AddMemberDTO;
import com.fullstack.backend.dto.request.CreateProjectDTO;
import com.fullstack.backend.dto.request.UpdateMemberRoleDTO;
import com.fullstack.backend.dto.request.UpdateProjectDTO;
import com.fullstack.backend.dto.response.MemberResponseDTO;
import com.fullstack.backend.dto.response.ProjectResponseDTO;
import com.fullstack.backend.entity.*;
import com.fullstack.backend.exception.DuplicateResourceException;
import com.fullstack.backend.exception.ResourceNotFoundException;
import com.fullstack.backend.repository.ProjectMemberRepository;
import com.fullstack.backend.repository.ProjectRepository;
import com.fullstack.backend.repository.UserRepository;
import com.fullstack.backend.util.SecurityUtils;
import com.fullstack.backend.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectService implements IProjectService{

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMemberRepository projectMemberRepository;

    /**
     * {@code @Transactional} is needed here to:
     * 1. Ensure database write operation is atomic
     * 2. Keep Hibernate session open during convertToDTO() call
     * 3. Allow access to lazy-loaded 'owner' relationship when converting to DTO
     */
    @Override
    @Transactional
    public ProjectResponseDTO createProject(CreateProjectDTO createProjectDTO) {
        User currentUser = SecurityUtils.getCurrentUser();
        Project project = new Project();
        project.setName(createProjectDTO.getName());
        project.setDescription(createProjectDTO.getDescription());
        project.setStartDate(createProjectDTO.getStartDate());
        project.setEndDate(createProjectDTO.getEndDate());
        project.setOwner(currentUser);
        project.setStatus(ProjectStatus.PLANNING);

        Project savedProject = projectRepository.save(project);
        return convertToDTO(savedProject);
    }

    /**
     * {@code @Transactional(readOnly = true)} is needed here to:
     * 1. Keep Hibernate session open while accessing lazy-loaded relationships
     * 2. Prevent LazyInitializationException when convertToDTO() accesses project.getOwner()
     * 3. readOnly=true is an optimization - tells database this is a read-only operation
     * 4. Without this, session closes after findById() and accessing owner fields throws exception
     */
    @Override
    @Transactional(readOnly = true)
    public ProjectResponseDTO getProjectById(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Project not found with id: " + id));
        return convertToDTO(project);
    }

    /**
     * {@code @Transactional(readOnly = true)} is needed here to:
     * 1. Keep Hibernate session open during the entire operation
     * 2. Prevent LazyInitializationException when map() calls convertToDTO()
     * 3. Each project's lazy-loaded 'owner' is accessed during DTO conversion
     * 4. Without this, session closes after findAll() and map() fails when accessing owner
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProjectResponseDTO> getAllProjects(Pageable pageable) {
        return projectRepository.findAll(pageable).map(this::convertToDTO);
    }

    /**
     * {@code @Transactional} is needed here to:
     * 1. Ensure atomic database update operation
     * 2. Keep session open for accessing lazy-loaded 'owner' in checkOwnershipOrAdmin()
     * 3. Allow convertToDTO() to access owner fields after save()
     * 4. Rollback changes if any exception occurs during the operation
     */
    @Override
    @Transactional
    public ProjectResponseDTO updateProject(Long id, UpdateProjectDTO dto) {
        Project project = projectRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Project not found with id: " + id));
        User currentUser = SecurityUtils.getCurrentUser();
        checkOwnershipOrAdmin(project,currentUser);
        project.setDescription(dto.getDescription());
        project.setName(dto.getName());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());
        Project savedProject = projectRepository.save(project);
        return convertToDTO(savedProject);
    }

    /**
     * {@code @Transactional} is needed here to:
     * 1. Ensure atomic delete operation
     * 2. Keep session open for accessing lazy-loaded 'owner' in checkOwnershipOrAdmin()
     * 3. Rollback if permission check or delete fails
     * 4. Handle cascade deletes properly (e.g., project members, tasks)
     */
    @Override
    @Transactional
    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Project not found with id: " + id));
        User currentUser = SecurityUtils.getCurrentUser();
        checkOwnershipOrAdmin(project,currentUser);
        projectRepository.deleteById(id);
    }

    /**
     * {@code @Transactional} is needed here to:
     * 1. Ensure atomic status update operation
     * 2. Keep session open for checkOwnershipOrAdmin() to access lazy owner
     * 3. Allow convertToDTO() to access owner after save()
     * 4. Rollback if permission check fails
     */
    @Override
    @Transactional
    public ProjectResponseDTO updateProjectStatus(Long id, ProjectStatus status) {
        Project project = projectRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Project not found with id: " + id));
        User currentUser = SecurityUtils.getCurrentUser();
        checkOwnershipOrAdmin(project,currentUser);
        project.setStatus(status);
        Project savedProject = projectRepository.save(project);
        return convertToDTO(savedProject);
    }

    /**
     * {@code @Transactional(readOnly = true)} is needed here to:
     * 1. Keep session open during search and DTO conversion
     * 2. Allow map() operation to access lazy-loaded owner for each project
     * 3. Prevent LazyInitializationException during pagination
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProjectResponseDTO> searchProjectsByName(String name, Pageable pageable) {
        return projectRepository.findByNameContainingIgnoreCase(name, pageable).map(this::convertToDTO);
    }

    /**
     * {@code @Transactional(readOnly = true)} is needed here to:
     * 1. Keep session open during status filtering and DTO conversion
     * 2. Allow map() to access lazy-loaded owner for filtered projects
     * 3. Handle pagination without LazyInitializationException
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProjectResponseDTO> searchProjectsByStatus(ProjectStatus status, Pageable pageable) {
        return projectRepository.findByStatus(status,pageable).map(this::convertToDTO);
    }

    /**
     * {@code @Transactional(readOnly = true)} is needed here to:
     * 1. Keep session open while fetching user's owned projects
     * 2. Allow map() to access lazy owner during DTO conversion
     * 3. Ensure pagination works without session closure issues
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProjectResponseDTO> getMyProjects(Pageable pageable) {
        User currentUser = SecurityUtils.getCurrentUser();
        Page<Project> projects = projectRepository.findMyProjects(currentUser.getId(),pageable);
        return projects.map(this::convertToDTO);
    }

    /**
     * {@code @Transactional} is needed here to:
     * 1. Ensure atomic member addition operation
     * 2. Keep session open for accessing lazy project owner in checkOwnerOrAdminPermission()
     * 3. Allow convertToMemberDTO() to access lazy-loaded user and project fields
     * 4. Rollback entire operation if any step fails (validation, permission check, or save)
     */
    @Transactional
    public MemberResponseDTO addMember(Long projectId, AddMemberDTO dto) {
        // 1. Validate that project exists
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        // 2. Validate that user to be added exists
        User userToAdd = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));

        // 3. Check if user is already a member
        if (projectMemberRepository.existsByProjectIdAndUserId(projectId, dto.getUserId())) {
            throw new DuplicateResourceException("User is already a member of this project");
        }

        // 4. Permission check: Only owner or admin can add members
        User currentUser = SecurityUtils.getCurrentUser();
        checkOwnerOrAdminPermission(project, currentUser);

        // 5. Create and save new member
        ProjectMember newMember = new ProjectMember();
        newMember.setProject(project);
        newMember.setUser(userToAdd);
        newMember.setRole(dto.getRole());

        ProjectMember savedMember = projectMemberRepository.save(newMember);

        // 6. Convert to DTO and return
        return convertToMemberDTO(savedMember);
    }

    /**
     * {@code @Transactional} is needed here to:
     * 1. Ensure atomic member removal operation
     * 2. Keep session open for accessing lazy project owner (step 3)
     * 3. Keep session open during permission check in checkOwnerOrAdminPermission()
     * 4. Rollback if permission check fails or delete encounters an error
     */
    @Transactional
    public void removeMember(Long projectId, Long userId) {
        // 1. Validate that project exists
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        // 2. Check if member exists in the project
        ProjectMember member = projectMemberRepository.findByProjectIdAndUserId(projectId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("User is not a member of this project"));

        // 3. Prevent removing the project owner
        if (project.getOwner().getId().equals(userId)) {
            throw new AccessDeniedException("Cannot remove the project owner from members");
        }

        // 4. Permission check: Only owner or admin can remove members
        User currentUser = SecurityUtils.getCurrentUser();
        checkOwnerOrAdminPermission(project, currentUser);

        // 5. Delete the member
        projectMemberRepository.deleteByProjectIdAndUserId(projectId, userId);
    }

    /**
     * {@code @Transactional} is needed here to:
     * 1. Ensure atomic role update operation
     * 2. Keep session open for accessing lazy project owner (step 3)
     * 3. Keep session open during checkOwnerOrAdminPermission()
     * 4. Allow convertToMemberDTO() to access lazy user and project after update
     * 5. Rollback if any validation or permission check fails
     */
    @Transactional
    public MemberResponseDTO updateMemberRole(Long projectId, Long userId, UpdateMemberRoleDTO dto) {
        // 1. Validate that project exists
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        // 2. Find the member
        ProjectMember member = projectMemberRepository.findByProjectIdAndUserId(projectId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("User is not a member of this project"));

        // 3. Prevent changing owner's role
        if (project.getOwner().getId().equals(userId)) {
            throw new AccessDeniedException("Cannot change role of project owner");
        }

        // 4. Permission check: Only owner or admin can update roles
        User currentUser = SecurityUtils.getCurrentUser();
        checkOwnerOrAdminPermission(project, currentUser);

        // 5. Update the role
        member.setRole(dto.getRole());
        ProjectMember updatedMember = projectMemberRepository.save(member);

        // 6. Convert to DTO and return
        return convertToMemberDTO(updatedMember);
    }

    /**
     * {@code @Transactional(readOnly = true)} is needed here to:
     * 1. Keep session open during entire member list fetch and conversion
     * 2. Allow checkProjectMembership() to access lazy project owner
     * 3. Allow stream().map() to access lazy user and project fields in each ProjectMember
     * 4. Prevent LazyInitializationException when convertToMemberDTO() is called for each member
     */
    @Transactional(readOnly = true)
    public List<MemberResponseDTO> getProjectMembers(Long projectId) {
        // 1. Validate that project exists
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        // 2. Check if current user has access to view members
        // (Anyone who is owner or member can view the team)
        User currentUser = SecurityUtils.getCurrentUser();
        checkProjectMembership(project, currentUser);

        // 3. Get all members and convert to DTOs
        List<ProjectMember> members = projectMemberRepository.findByProjectId(projectId);
        return members.stream()
                .map(this::convertToMemberDTO)
                .collect(Collectors.toList());
    }

    private ProjectResponseDTO convertToDTO(Project project) {
        return new ProjectResponseDTO(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getStatus(),
                project.getCreatedAt(),
                project.getUpdatedAt(),
                project.getStartDate(),
                project.getEndDate(),
                UserMapper.toSummaryDTO(project.getOwner())
        );
    }
    private void checkOwnershipOrAdmin(Project project, User currentUser) {
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        boolean isOwner = project.getOwner().getId().equals(currentUser.getId());
        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException(
                    "You don't have permission to modify this project"
            );
        }
    }

    /**
     * Check if current user is project owner OR has ADMIN role in project members
     * This is used for member management operations
     */
    private void checkOwnerOrAdminPermission(Project project, User currentUser) {
        // Check if user is the project owner
        boolean isProjectOwner = project.getOwner().getId().equals(currentUser.getId());

        if (isProjectOwner) {
            return; // Owner always has permission
        }

        // If not owner, check if they're a project member with ADMIN role
        Optional<ProjectMember> memberOptional = projectMemberRepository
                .findByProjectIdAndUserId(project.getId(), currentUser.getId());

        if (memberOptional.isEmpty()) {
            throw new AccessDeniedException("You are not a member of this project");
        }

        ProjectMember member = memberOptional.get();
        if (member.getRole() != ProjectRole.ADMIN) {
            throw new AccessDeniedException("Only project owner or admins can perform this action");
        }
    }

    /**
     * Check if current user has access to the project (owner or any member)
     * This is used for read operations like viewing members or tasks
     */
    private void checkProjectMembership(Project project, User currentUser) {
        boolean isOwner = project.getOwner().getId().equals(currentUser.getId());
        boolean isMember = projectMemberRepository.existsByProjectIdAndUserId(
                project.getId(), currentUser.getId());

        if (!isOwner && !isMember) {
            throw new AccessDeniedException("You don't have access to this project");
        }
    }

    /**
     * Convert ProjectMember entity to MemberResponseDTO
     */
    private MemberResponseDTO convertToMemberDTO(ProjectMember member) {
        return new MemberResponseDTO(
                member.getProject().getId(),
                UserMapper.toSummaryDTO(member.getUser()),
                member.getRole(),
                member.getJoinedAt()
        );
    }
}
