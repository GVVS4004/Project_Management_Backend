CREATE TABLE tasks (
    id BIGSERIAL PRIMARY KEY,

    title VARCHAR(500) NOT NULL,
    description VARCHAR(5000),

    status VARCHAR(20) NOT NULL DEFAULT 'TODO',
    priority VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',
    type VARCHAR(20) NOT NULL DEFAULT 'TASK',

    project_id BIGINT NOT NULL,
    created_by_id BIGINT NOT NULL,
    assigned_to_id BIGINT,
    parent_task_id BIGINT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    due_date DATE,
    completed_at TIMESTAMP,

    CONSTRAINT fk_tasks_project FOREIGN KEY (project_id)
        REFERENCES projects(id) ON DELETE RESTRICT,

    CONSTRAINT fk_tasks_created_by FOREIGN KEY (created_by_id)
        REFERENCES users(id) ON DELETE RESTRICT,

    CONSTRAINT fk_tasks_assigned_to FOREIGN KEY (assigned_to_id)
        REFERENCES users(id) ON DELETE SET NULL,

    CONSTRAINT fk_tasks_parent FOREIGN KEY (parent_task_id)
        REFERENCES tasks(id) ON DELETE RESTRICT
);

ALTER TABLE tasks ADD CONSTRAINT chk_tasks_status
      CHECK (status IN ('TODO', 'IN_PROGRESS', 'BLOCKED', 'REVIEW', 'TESTING', 'DONE', 'ABANDONED'));

ALTER TABLE tasks ADD CONSTRAINT chk_tasks_priority
      CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH', 'CRITICAL'));

ALTER TABLE tasks ADD CONSTRAINT chk_tasks_type
      CHECK (type IN ('STORY', 'BUG', 'DEFECT', 'EPIC', 'TASK', 'SUBTASK'));


-- Indexes for performance
  CREATE INDEX idx_tasks_project_id ON tasks(project_id);
  CREATE INDEX idx_tasks_created_by_id ON tasks(created_by_id);
  CREATE INDEX idx_tasks_assigned_to_id ON tasks(assigned_to_id);
  CREATE INDEX idx_tasks_parent_task_id ON tasks(parent_task_id);
  CREATE INDEX idx_tasks_status ON tasks(status);
  CREATE INDEX idx_tasks_priority ON tasks(priority);
  CREATE INDEX idx_tasks_type ON tasks(type);
  CREATE INDEX idx_tasks_created_at ON tasks(created_at);

  -- Composite indexes for common queries
  CREATE INDEX idx_tasks_project_status ON tasks(project_id, status);
  CREATE INDEX idx_tasks_assigned_status ON tasks(assigned_to_id, status);

  -- Comments for documentation
  COMMENT ON TABLE tasks IS 'Stores tasks/issues for project management with Jira-style hierarchy';
  COMMENT ON COLUMN tasks.parent_task_id IS 'Self-referential FK for task hierarchy (Epic->Story->Subtask)';
  COMMENT ON COLUMN tasks.type IS 'Task type: EPIC (top level), STORY/TASK/BUG (can have subtasks), SUBTASK (leaf node)';