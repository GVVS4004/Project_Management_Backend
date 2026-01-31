CREATE TABLE refresh_tokens (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(500) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    device_info VARCHAR(255),
    ip_address VARCHAR(45),

    CONSTRAINT fK_refresh_token_user FOREIGN KEY (user_id)
    REFERENCES users(id) ON DELETE CASCADE
);
  -- Create index on user_id for faster lookups
  CREATE INDEX idx_refresh_tokens_user_id ON refresh_tokens(user_id);

  -- Create index on token for faster lookups
  CREATE INDEX idx_refresh_tokens_token ON refresh_tokens(token);

  -- Create index on expiry_date for cleanup queries
  CREATE INDEX idx_refresh_tokens_expiry_date ON refresh_tokens(expiry_date);

  -- Add comments
  COMMENT ON TABLE refresh_tokens IS 'Stores JWT refresh tokens for user authentication';
  COMMENT ON COLUMN refresh_tokens.token IS 'The refresh token string (JWT)';
  COMMENT ON COLUMN refresh_tokens.expiry_date IS 'When the refresh token expires';
  COMMENT ON COLUMN refresh_tokens.device_info IS 'Device/browser information';
  COMMENT ON COLUMN refresh_tokens.ip_address IS 'IP address where token was created';
