package com.fullstack.backend.repository;

import com.fullstack.backend.entity.Project;
import com.fullstack.backend.entity.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Page<Project> findByOwnerId(Long ownerId, Pageable pageable);

    Page<Project> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Project> findByStatus(ProjectStatus status, Pageable pageable);

    boolean existsById(Long id);
    // 4. Check if a project with exact name exists (useful for validation)
    boolean existsByName(String name);

    // Find projects by owner AND status
    List<Project> findByOwnerIdAndStatus(Long ownerId, ProjectStatus status);

    // Find all projects ordered by creation date (newest first)
    List<Project> findAllByOrderByCreatedAtDesc();
}
