  -- Create media table for storing profile images
  CREATE TABLE media (
      id BIGSERIAL PRIMARY KEY,
      file_name VARCHAR(255) NOT NULL,
      file_type VARCHAR(100) NOT NULL,
      file_size BIGINT NOT NULL,
      data BYTEA NOT NULL,
      user_id BIGINT NOT NULL,
      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
      CONSTRAINT fk_media_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
  );

  -- Create index on user_id for faster lookups
  CREATE INDEX idx_media_user_id ON media(user_id);

  -- Add comment to table
  COMMENT ON TABLE media IS 'Stores user profile images and other media files';
  COMMENT ON COLUMN media.data IS 'Binary data of the uploaded file';
  COMMENT ON COLUMN media.file_size IS 'File size in bytes';