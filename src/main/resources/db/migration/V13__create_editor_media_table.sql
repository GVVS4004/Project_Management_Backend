CREATE TABLE editor_media (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    storage_key VARCHAR(500) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    file_size BIGINT NOT NULL,
    uploaded_by_id BIGINT NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
    task_id BIGINT REFERENCES tasks(id) ON DELETE SET NULL,
    comment_id BIGINT REFERENCES comments(id) ON DELETE SET NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_editor_media_storage_key UNIQUE (storage_key)
);

CREATE INDEX idx_editor_media_task_id ON editor_media(task_id);
CREATE INDEX idx_editor_media_comment_id ON editor_media(comment_id);
CREATE INDEX idx_editor_media_uploaded_by_id ON editor_media(uploaded_by_id);