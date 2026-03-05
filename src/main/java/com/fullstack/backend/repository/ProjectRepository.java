package com.fullstack.backend.repository;

import com.fullstack.backend.entity.Project;
import com.fullstack.backend.entity.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    @Query("SELECT p FROM Project p WHERE p.owner.id=:userId OR EXISTS(SELECT pm FROM ProjectMember pm WHERE pm.project=p AND pm.user.id= :userId)")
    Page<Project> findMyProjects(Long userId, Pageable pageable);
}
