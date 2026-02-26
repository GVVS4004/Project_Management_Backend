-- Create comments table for task discussions (Jira Cloud style)
-- Self-referential relationship with 1-level threading (max depth = 1)

CREATE TABLE comments (
    -- Primary key
    id BIGSERIAL PRIMARY KEY,

    -- Content
    content VARCHAR(5000) NOT NULL,

    -- Relationships (foreign keys)
    task_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    parent_comment_id BIGINT,  -- NULL = top-level, NOT NULL = reply

    -- Threading
    depth INTEGER NOT NULL DEFAULT 0,  -- 0 = top-level, 1 = reply

    -- Audit fields
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,  -- NULL until first edit
    is_edited BOOLEAN NOT NULL DEFAULT FALSE,

    -- Soft delete fields
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP,
    deleted_by BIGINT,

    -- Foreign key constraints
    CONSTRAINT fk_comments_tasks FOREIGN KEY (task_id)
        REFERENCES tasks(id) ON DELETE RESTRICT,

    CONSTRAINT fk_comments_users FOREIGN KEY (author_id)
        REFERENCES users(id) ON DELETE RESTRICT,

    CONSTRAINT fk_comments_parent FOREIGN KEY (parent_comment_id)
        REFERENCES comments(id) ON DELETE SET NULL,

    -- Business rule constraints
    CONSTRAINT chk_comments_depth
        CHECK (depth >= 0 AND depth <= 1),

    CONSTRAINT chk_comments_content
        CHECK (LENGTH(TRIM(content)) > 0)
);

-- Indexes for performance
CREATE INDEX idx_comments_task_id ON comments(task_id);
CREATE INDEX idx_comments_author_id ON comments(author_id);
CREATE INDEX idx_comments_parent_comment_id ON comments(parent_comment_id);
CREATE INDEX idx_comments_created_at ON comments(created_at DESC);
CREATE INDEX idx_comments_task_depth ON comments(task_id, depth);

-- Documentation comments
COMMENT ON TABLE comments IS 'Task comments with 1-level threading (Jira Cloud style)';
COMMENT ON COLUMN comments.parent_comment_id IS 'NULL = top-level comment, NOT NULL = reply to another comment';
COMMENT ON COLUMN comments.depth IS '0 = top-level comment, 1 = reply (maximum 1 level of nesting allowed)';
COMMENT ON COLUMN comments.is_deleted IS 'Soft delete flag - when true, show [Comment deleted] placeholder';
COMMENT ON COLUMN comments.deleted_by IS 'User ID who deleted the comment (author or admin)';