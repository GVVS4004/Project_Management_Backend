CREATE TABLE attachments (
    id BIGSERIAL PRIMARY KEY,

    file_name VARCHAR(255) NOT NULL,
    stored_file_name VARCHAR(255) NOT NULL,
    file_size BIGINT NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    storage_key VARCHAR(500) NOT NULL,

    task_id BIGINT NOT NULL,
    uploaded_by_id BIGINT NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_attachments_tasks FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
    CONSTRAINT fk_attachments_users FOREIGN KEY (uploaded_by_id) REFERENCES users(id) ON DELETE RESTRICT,

    CONSTRAINT chk_attachments_file_size CHECK (file_size > 0),

    CONSTRAINT chk_attachments_file_name CHECK (LENGTH(TRIM(file_name)) > 0 ),
    CONSTRAINT uq_attachments_storage_key UNIQUE (storage_key)
);


CREATE INDEX idx_attachments_task_id ON attachments(task_id);
CREATE INDEX idx_attachments_uploaded_by_id ON attachments(uploaded_by_id);
CREATE INDEX idx_attachments_created_at ON attachments(created_at);

COMMENT ON TABLE attachments IS 'File attachments for tasks (stored in S3/R2)';
COMMENT ON COLUMN attachments.stored_file_name IS 'UUID-based unique name to avoid collisions';
COMMENT ON COLUMN attachments.storage_key IS 'Full path/key in S3/R2 bucket';