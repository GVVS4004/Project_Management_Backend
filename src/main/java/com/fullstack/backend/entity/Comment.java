package com.fullstack.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comments")
@NamedEntityGraph(
        name = "Comment.withRelationships",
        attributeNodes = {
                @NamedAttributeNode("author"),
                @NamedAttributeNode("parentComment"),
                @NamedAttributeNode("task")
        }
)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 5000, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id" ,nullable= false)
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Comment> replies = new ArrayList<>();

    @Column(nullable = false)
    private Integer depth = 0;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private Boolean isEdited = false;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @Column
    private LocalDateTime deletedAt;

    @Column
    private Long deletedBy;

    @PrePersist
    protected void onCreate(){
        createdAt = LocalDateTime.now();
        if( parentComment != null ){
            depth = parentComment.getDepth() + 1;
        }
    }

    @PreUpdate
    protected  void onUpdate(){
        updatedAt = LocalDateTime.now();
    }

}
