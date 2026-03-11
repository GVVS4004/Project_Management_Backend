package com.fullstack.backend.repository;

import com.fullstack.backend.entity.ProjectMember;
import com.fullstack.backend.entity.ProjectRole;
import com.fullstack.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    public List<ProjectMember> findByProjectId(Long projectId);
    public List<ProjectMember> findByUserId(Long userId);
    public List<ProjectMember> findByProjectIdAndRole(Long projectId, ProjectRole role);
    public Optional<ProjectMember> findByProjectIdAndUserId(Long projectId, Long userId);
    public boolean existsByProjectIdAndUserId(Long projectId, Long userId);
    public void deleteByProjectIdAndUserId(Long projectId, Long userId);
    public long countByProjectId(Long projectId);
}
