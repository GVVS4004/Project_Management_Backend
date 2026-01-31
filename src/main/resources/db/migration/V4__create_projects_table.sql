CREATE TABLE projects (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(2000),
    status VARCHAR(20) NOT NULL DEFAULT 'PLANNING',
    owner_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_projects_user FOREIGN KEY (owner_id)
    REFERENCES users(id) ON DELETE RESTRICT
);


ALTER TABLE projects ADD CONSTRAINT chk_projects_status
    CHECK (status IN ('PLANNING', 'ACTIVE', 'ON_HOLD', 'COMPLETED', 'ABANDONED'));
CREATE INDEX idx_projects_owner_id ON projects(owner_id);
CREATE INDEX idx_projects_status ON projects(status);
CREATE INDEX idx_projects_name ON projects(name);
CREATE INDEX idx_projects_created_at ON projects(created_at);

COMMENT ON TABLE projects IS 'Stores project information for task management';
COMMENT ON COLUMN projects.owner_id IS 'References the user who created/owns the project';
COMMENT ON COLUMN projects.status IS 'Current lifecycle status of the project';