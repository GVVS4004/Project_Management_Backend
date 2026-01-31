-- Migration: Update ProjectRole enum - remove OWNER, keep only ADMIN and MEMBER
-- Reason: Project owner is now stored in projects.owner_id, not in project_members

-- Step 1: Remove any existing project_members with role='OWNER' (if any)
-- This is safe because owner is now tracked separately in projects.owner_id
DELETE FROM project_members WHERE role = 'OWNER';

-- Step 2: Drop the old CHECK constraint
ALTER TABLE project_members
    DROP CONSTRAINT IF EXISTS chk_project_members_role;

-- Step 3: Add new CHECK constraint with only ADMIN and MEMBER
ALTER TABLE project_members
    ADD CONSTRAINT chk_project_members_role
    CHECK (role IN ('ADMIN', 'MEMBER'));

-- Step 4: Update table comment to reflect the change
COMMENT ON COLUMN project_members.role IS 'Role within the project: ADMIN or MEMBER (Owner is tracked in projects.owner_id)';
