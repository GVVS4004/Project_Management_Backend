package com.fullstack.backend.repository;

import com.fullstack.backend.entity.Project;
import com.fullstack.backend.entity.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    // Single field filters
    Page<Project> findByOwnerId(Long ownerId, Pageable pageable);
    Page<Project> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Project> findByStatus(ProjectStatus status, Pageable pageable);

    // Two field combinations
    Page<Project> findByNameContainingIgnoreCaseAndStatus(String name, ProjectStatus status, Pageable pageable);
    Page<Project> findByNameContainingIgnoreCaseAndOwnerId(String name, Long ownerId, Pageable pageable);
    Page<Project> findByStatusAndOwnerId(ProjectStatus status, Long ownerId, Pageable pageable);

    // All three fields
    Page<Project> findByNameContainingIgnoreCaseAndStatusAndOwnerId(String name, ProjectStatus status, Long ownerId, Pageable pageable);

    boolean existsById(Long id);
    boolean existsByName(String name);

    List<Project> findByOwnerIdAndStatus(Long ownerId, ProjectStatus status);
    List<Project> findAllByOrderByCreatedAtDesc();

    @Query("SELECT p FROM Project p WHERE p.owner.id = :userId OR EXISTS(SELECT pm FROM ProjectMember pm WHERE pm.project = p AND pm.user.id = :userId)")
    Page<Project> findMyProjects(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT DISTINCT p.id FROM Project p LEFT JOIN ProjectMember pm ON pm.project.id = p.id WHERE p.owner.id = :userId OR pm.user.id = :userId")
    List<Long> findProjectIdsByUserId(@Param("userId") Long userId);

}
