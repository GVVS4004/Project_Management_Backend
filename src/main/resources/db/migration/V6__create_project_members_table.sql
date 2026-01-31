  -- Create project_members table (junction table for users and projects)
  CREATE TABLE project_members (
      id BIGSERIAL PRIMARY KEY,
      project_id BIGINT NOT NULL,
      user_id BIGINT NOT NULL,
      role VARCHAR(20) NOT NULL,
      joined_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
  );

  -- Foreign key constraints
  ALTER TABLE project_members
      ADD CONSTRAINT fk_project_members_project
      FOREIGN KEY (project_id)
      REFERENCES projects(id)
      ON DELETE CASCADE;

  ALTER TABLE project_members
      ADD CONSTRAINT fk_project_members_user
      FOREIGN KEY (user_id)
      REFERENCES users(id)
      ON DELETE CASCADE;

  -- Unique constraint: same user can't be in same project twice
  ALTER TABLE project_members
      ADD CONSTRAINT uk_project_members_project_user
      UNIQUE (project_id, user_id);

  -- Check constraint: only valid roles allowed
  ALTER TABLE project_members
      ADD CONSTRAINT chk_project_members_role
      CHECK (role IN ('OWNER', 'ADMIN', 'MEMBER'));

  -- Indexes for performance
  CREATE INDEX idx_project_members_project_id ON project_members(project_id);
  CREATE INDEX idx_project_members_user_id ON project_members(user_id);
  CREATE INDEX idx_project_members_role ON project_members(role);
  CREATE INDEX idx_project_members_project_role ON project_members(project_id, role);

  -- Table and column comments
  COMMENT ON TABLE project_members IS 'Junction table for Many-to-Many relationship between users and projects';
  COMMENT ON COLUMN project_members.project_id IS 'References the project this membership belongs to';
  COMMENT ON COLUMN project_members.user_id IS 'References the user who is a member';
  COMMENT ON COLUMN project_members.role IS 'Role within the project: OWNER, ADMIN, or MEMBER';
  COMMENT ON COLUMN project_members.joined_at IS 'When the user was added to the project';