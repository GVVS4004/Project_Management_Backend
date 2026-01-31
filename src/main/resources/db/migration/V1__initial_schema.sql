-- Initial schema for Task Management Platform
-- Creates users table with authentication and role support

-- Create users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    password VARCHAR(255) NOT NULL,
    profile_image_url VARCHAR(255),
    role VARCHAR(20) NOT NULL DEFAULT 'MEMBER',
    enabled BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Create indexes for better query performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_role ON users(role);

-- Add constraints to ensure role values are valid
ALTER TABLE users ADD CONSTRAINT chk_role
    CHECK (role IN ('ADMIN', 'MANAGER', 'MEMBER'));

-- Optional: Insert a default admin user for testing
-- Uncomment the below INSERT statement if you want a pre-created admin user
-- Otherwise, use the registration endpoint to create your first user

-- INSERT INTO users (username, email, first_name, last_name, password, role, enabled, created_at, updated_at)
-- VALUES (
--     'admin',
--     'admin@taskmanagement.com',
--     'Admin',
--     'User',
--     '$2a$10$N9qo8uLOickgx2ZMRZoMye1p0XhMkrcVXqXVXqXVXqXVXqXVXqXVq', -- password: admin123
--     'ADMIN',
--     true,
--     CURRENT_TIMESTAMP,
--     CURRENT_TIMESTAMP
-- );

-- Note: To create your first admin user, use the registration endpoint:
-- POST /api/auth/register with role: "ADMIN"
