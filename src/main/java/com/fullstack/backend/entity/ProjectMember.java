package com.fullstack.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "project_members")
@NamedEntityGraph(
        name = "ProjectMember.withRelationships",
        attributeNodes = {
                @NamedAttributeNode("user"),
                @NamedAttributeNode("project")
        }
)
public class ProjectMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many members can belong to ONE project
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    // Many memberships can belong to ONE user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private ProjectRole role;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime joinedAt;
}
