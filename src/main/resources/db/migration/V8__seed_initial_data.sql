-- ============================================================================
-- Seed Data Migration - Comprehensive Test Data
-- Purpose: Populate database with extensive test data for UI development
-- Password for all users: password123
-- BCrypt hash: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
-- ============================================================================

-- ============================================================================
-- USERS (25 Users)
-- ============================================================================

-- Admin Users (5)
INSERT INTO users (user_name, first_name, last_name, email, password, role, enabled, created_at, updated_at)
VALUES
    ('admin', 'System', 'Administrator', 'admin@example.com',
     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
     'ADMIN', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('john_admin', 'John', 'Smith', 'john.smith@example.com',
     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
     'ADMIN', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('jennifer_admin', 'Jennifer', 'Taylor', 'jennifer.taylor@example.com',
     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
     'ADMIN', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('robert_admin', 'Robert', 'Anderson', 'robert.anderson@example.com',
     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
     'ADMIN', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('lisa_admin', 'Lisa', 'Thompson', 'lisa.thompson@example.com',
     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
     'ADMIN', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Manager Users (8)
INSERT INTO users (user_name, first_name, last_name, email, password, role, enabled, created_at, updated_at)
VALUES
    ('sarah_manager', 'Sarah', 'Johnson', 'sarah.johnson@example.com',
     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
     'MANAGER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('mike_manager', 'Mike', 'Brown', 'mike.brown@example.com',
     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
     'MANAGER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('emily_manager', 'Emily', 'White', 'emily.white@example.com',
     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
     'MANAGER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('james_manager', 'James', 'Martin', 'james.martin@example.com',
     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
     'MANAGER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('michelle_manager', 'Michelle', 'Lee', 'michelle.lee@example.com',
     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
     'MANAGER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('daniel_manager', 'Daniel', 'Harris', 'daniel.harris@example.com',
     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
     'MANAGER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('laura_manager', 'Laura', 'Clark', 'laura.clark@example.com',
     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
     'MANAGER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('kevin_manager', 'Kevin', 'Lewis', 'kevin.lewis@example.com',
     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
     'MANAGER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Member Users (12)
INSERT INTO users (user_name, first_name, last_name, email, password, role, enabled, created_at, updated_at)
VALUES
    ('alice_dev', 'Alice', 'Williams', 'alice.williams@example.com',
     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
     'MEMBER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('bob_dev', 'Bob', 'Davis', 'bob.davis@example.com',
     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
     'MEMBER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('carol_designer', 'Carol', 'Martinez', 'carol.martinez@example.com',
     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
     'MEMBER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('david_qa', 'David', 'Garcia', 'david.garcia@example.com',
     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
     'MEMBER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('emma_dev', 'Emma', 'Rodriguez', 'emma.rodriguez@example.com',
     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
     'MEMBER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('frank_dev', 'Frank', 'Wilson', 'frank.wilson@example.com',
     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
     'MEMBER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('grace_designer', 'Grace', 'Moore', 'grace.moore@example.com',
     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
     'MEMBER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('henry_qa', 'Henry', 'Jackson', 'henry.jackson@example.com',
     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
     'MEMBER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('isabel_dev', 'Isabel', 'Thomas', 'isabel.thomas@example.com',
     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
     'MEMBER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('jack_dev', 'Jack', 'Young', 'jack.young@example.com',
     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
     'MEMBER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('kate_designer', 'Kate', 'King', 'kate.king@example.com',
     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
     'MEMBER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('liam_qa', 'Liam', 'Wright', 'liam.wright@example.com',
     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
     'MEMBER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ============================================================================
-- PROJECTS (25 Projects)
-- ============================================================================

-- ACTIVE Projects (10)
INSERT INTO projects (name, description, status, start_date, end_date, owner_id, created_at, updated_at)
VALUES
    ('E-Commerce Platform',
     'Building a modern e-commerce platform with microservices architecture. Features include product catalog, shopping cart, payment integration, and order management.',
     'ACTIVE', '2025-01-01', '2025-06-30',
     (SELECT id FROM users WHERE user_name = 'sarah_manager'),
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Mobile Banking App',
     'Developing a secure mobile banking application for iOS and Android. Includes account management, transfers, bill payments, and investment tracking.',
     'ACTIVE', '2025-02-01', '2025-08-31',
     (SELECT id FROM users WHERE user_name = 'mike_manager'),
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Customer Portal Redesign',
     'Complete redesign of the customer portal with modern UI/UX. Focus on accessibility, performance, and user experience improvements.',
     'ACTIVE', '2025-01-15', '2025-04-30',
     (SELECT id FROM users WHERE user_name = 'john_admin'),
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Healthcare Management System',
     'Comprehensive healthcare management platform for hospitals. Patient records, appointment scheduling, billing, and telemedicine features.',
     'ACTIVE', '2025-01-20', '2025-12-31',
     (SELECT id FROM users WHERE user_name = 'emily_manager'),
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Real-Time Chat Application',
     'Building a scalable real-time chat application with WebSocket support. Features include group chats, file sharing, and video calls.',
     'ACTIVE', '2025-02-10', '2025-07-15',
     (SELECT id FROM users WHERE user_name = 'james_manager'),
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('AI-Powered Recommendation Engine',
     'Developing machine learning models for personalized product recommendations. Integration with existing e-commerce platforms.',
     'ACTIVE', '2025-01-05', '2025-09-30',
     (SELECT id FROM users WHERE user_name = 'michelle_manager'),
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Supply Chain Optimization',
     'Optimizing supply chain operations with predictive analytics and automated workflows. Real-time tracking and inventory management.',
     'ACTIVE', '2025-02-15', '2025-10-31',
     (SELECT id FROM users WHERE user_name = 'daniel_manager'),
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Social Media Analytics Platform',
     'Building an analytics platform for social media metrics. Sentiment analysis, engagement tracking, and competitor analysis.',
     'ACTIVE', '2025-01-25', '2025-08-15',
     (SELECT id FROM users WHERE user_name = 'laura_manager'),
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('IoT Device Management',
     'Platform for managing IoT devices at scale. Device provisioning, monitoring, firmware updates, and data collection.',
     'ACTIVE', '2025-02-05', '2025-11-30',
     (SELECT id FROM users WHERE user_name = 'kevin_manager'),
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Learning Management System',
     'Educational platform with course management, video streaming, assignments, and progress tracking. Gamification features included.',
     'ACTIVE', '2025-01-10', '2025-06-15',
     (SELECT id FROM users WHERE user_name = 'jennifer_admin'),
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- PLANNING Projects (6)
INSERT INTO projects (name, description, status, start_date, end_date, owner_id, created_at, updated_at)
VALUES
    ('Data Analytics Dashboard',
     'Creating a comprehensive analytics dashboard for business intelligence. Will integrate with multiple data sources and provide real-time insights.',
     'PLANNING', '2025-03-01', '2025-09-30',
     (SELECT id FROM users WHERE user_name = 'sarah_manager'),
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('API Gateway Implementation',
     'Implementing a centralized API gateway for microservices. Includes authentication, rate limiting, and request routing.',
     'PLANNING', '2025-04-01', '2025-07-31',
     (SELECT id FROM users WHERE user_name = 'admin'),
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Cloud Migration Strategy',
     'Planning migration of on-premise infrastructure to AWS. Cost analysis, security assessment, and implementation roadmap.',
     'PLANNING', '2025-03-15', '2025-12-31',
     (SELECT id FROM users WHERE user_name = 'robert_admin'),
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Employee Self-Service Portal',
     'Planning an HR self-service portal for employees. Leave management, payroll access, and performance reviews.',
     'PLANNING', '2025-04-10', '2025-10-31',
     (SELECT id FROM users WHERE user_name = 'emily_manager'),
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Cybersecurity Enhancement',
     'Comprehensive security audit and implementation of enhanced security measures. Penetration testing and compliance validation.',
     'PLANNING', '2025-03-20', '2025-08-31',
     (SELECT id FROM users WHERE user_name = 'lisa_admin'),
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Multi-Tenant SaaS Platform',
     'Designing a multi-tenant architecture for SaaS product. Tenant isolation, billing integration, and scalability planning.',
     'PLANNING', '2025-05-01', '2025-11-30',
     (SELECT id FROM users WHERE user_name = 'james_manager'),
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ON_HOLD Projects (3)
INSERT INTO projects (name, description, status, start_date, end_date, owner_id, created_at, updated_at)
VALUES
    ('Legacy System Migration',
     'Migration of legacy system to cloud infrastructure. On hold pending budget approval and resource allocation.',
     'ON_HOLD', '2024-11-01', '2025-05-31',
     (SELECT id FROM users WHERE user_name = 'mike_manager'),
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Warehouse Automation',
     'Automating warehouse operations with robotics and AI. On hold due to vendor contract negotiations.',
     'ON_HOLD', '2024-10-15', '2025-07-31',
     (SELECT id FROM users WHERE user_name = 'daniel_manager'),
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Virtual Reality Training',
     'VR-based training modules for employee onboarding. On hold pending hardware procurement.',
     'ON_HOLD', '2024-12-01', '2025-06-30',
     (SELECT id FROM users WHERE user_name = 'michelle_manager'),
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- COMPLETED Projects (4)
INSERT INTO projects (name, description, status, start_date, end_date, owner_id, created_at, updated_at)
VALUES
    ('Authentication Service',
     'Implemented JWT-based authentication service with OAuth2 integration. Successfully deployed to production.',
     'COMPLETED', '2024-09-01', '2024-12-15',
     (SELECT id FROM users WHERE user_name = 'john_admin'),
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Payment Gateway Integration',
     'Integrated multiple payment gateways (Stripe, PayPal, Square) with unified API. All transactions are secure and PCI compliant.',
     'COMPLETED', '2024-08-01', '2024-11-30',
     (SELECT id FROM users WHERE user_name = 'sarah_manager'),
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Email Notification System',
     'Built scalable email notification system with template management and delivery tracking. Handles 100k+ emails daily.',
     'COMPLETED', '2024-07-01', '2024-10-31',
     (SELECT id FROM users WHERE user_name = 'laura_manager'),
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Performance Optimization Phase 1',
     'Optimized database queries and implemented caching strategy. Reduced average response time by 60%.',
     'COMPLETED', '2024-06-15', '2024-09-30',
     (SELECT id FROM users WHERE user_name = 'kevin_manager'),
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ABANDONED Projects (2)
INSERT INTO projects (name, description, status, start_date, end_date, owner_id, created_at, updated_at)
VALUES
    ('Blockchain POC',
     'Proof of concept for blockchain integration. Abandoned due to high costs and unclear ROI.',
     'ABANDONED', '2024-06-01', '2024-08-31',
     (SELECT id FROM users WHERE user_name = 'admin'),
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Chatbot Implementation',
     'AI chatbot for customer support. Abandoned in favor of third-party solution with better features.',
     'ABANDONED', '2024-05-01', '2024-07-15',
     (SELECT id FROM users WHERE user_name = 'robert_admin'),
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ============================================================================
-- PROJECT MEMBERS (80+ Memberships)
-- ============================================================================

-- E-Commerce Platform Team
INSERT INTO project_members (project_id, user_id, role, joined_at)
VALUES
    ((SELECT id FROM projects WHERE name = 'E-Commerce Platform'), (SELECT id FROM users WHERE user_name = 'john_admin'), 'ADMIN', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'E-Commerce Platform'), (SELECT id FROM users WHERE user_name = 'alice_dev'), 'MEMBER', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'E-Commerce Platform'), (SELECT id FROM users WHERE user_name = 'bob_dev'), 'MEMBER', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'E-Commerce Platform'), (SELECT id FROM users WHERE user_name = 'carol_designer'), 'MEMBER', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'E-Commerce Platform'), (SELECT id FROM users WHERE user_name = 'david_qa'), 'MEMBER', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'E-Commerce Platform'), (SELECT id FROM users WHERE user_name = 'emma_dev'), 'MEMBER', CURRENT_TIMESTAMP);

-- Mobile Banking App Team
INSERT INTO project_members (project_id, user_id, role, joined_at)
VALUES
    ((SELECT id FROM projects WHERE name = 'Mobile Banking App'), (SELECT id FROM users WHERE user_name = 'sarah_manager'), 'ADMIN', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Mobile Banking App'), (SELECT id FROM users WHERE user_name = 'frank_dev'), 'MEMBER', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Mobile Banking App'), (SELECT id FROM users WHERE user_name = 'grace_designer'), 'MEMBER', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Mobile Banking App'), (SELECT id FROM users WHERE user_name = 'henry_qa'), 'MEMBER', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Mobile Banking App'), (SELECT id FROM users WHERE user_name = 'isabel_dev'), 'MEMBER', CURRENT_TIMESTAMP);

-- Customer Portal Redesign Team
INSERT INTO project_members (project_id, user_id, role, joined_at)
VALUES
    ((SELECT id FROM projects WHERE name = 'Customer Portal Redesign'), (SELECT id FROM users WHERE user_name = 'sarah_manager'), 'ADMIN', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Customer Portal Redesign'), (SELECT id FROM users WHERE user_name = 'carol_designer'), 'MEMBER', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Customer Portal Redesign'), (SELECT id FROM users WHERE user_name = 'kate_designer'), 'MEMBER', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Customer Portal Redesign'), (SELECT id FROM users WHERE user_name = 'alice_dev'), 'MEMBER', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Customer Portal Redesign'), (SELECT id FROM users WHERE user_name = 'bob_dev'), 'MEMBER', CURRENT_TIMESTAMP);

-- Healthcare Management System Team
INSERT INTO project_members (project_id, user_id, role, joined_at)
VALUES
    ((SELECT id FROM projects WHERE name = 'Healthcare Management System'), (SELECT id FROM users WHERE user_name = 'john_admin'), 'ADMIN', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Healthcare Management System'), (SELECT id FROM users WHERE user_name = 'mike_manager'), 'ADMIN', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Healthcare Management System'), (SELECT id FROM users WHERE user_name = 'jack_dev'), 'MEMBER', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Healthcare Management System'), (SELECT id FROM users WHERE user_name = 'isabel_dev'), 'MEMBER', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Healthcare Management System'), (SELECT id FROM users WHERE user_name = 'liam_qa'), 'MEMBER', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Healthcare Management System'), (SELECT id FROM users WHERE user_name = 'grace_designer'), 'MEMBER', CURRENT_TIMESTAMP);

-- Real-Time Chat Application Team
INSERT INTO project_members (project_id, user_id, role, joined_at)
VALUES
    ((SELECT id FROM projects WHERE name = 'Real-Time Chat Application'), (SELECT id FROM users WHERE user_name = 'emily_manager'), 'ADMIN', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Real-Time Chat Application'), (SELECT id FROM users WHERE user_name = 'frank_dev'), 'MEMBER', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Real-Time Chat Application'), (SELECT id FROM users WHERE user_name = 'emma_dev'), 'MEMBER', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Real-Time Chat Application'), (SELECT id FROM users WHERE user_name = 'henry_qa'), 'MEMBER', CURRENT_TIMESTAMP);

-- AI-Powered Recommendation Engine Team
INSERT INTO project_members (project_id, user_id, role, joined_at)
VALUES
    ((SELECT id FROM projects WHERE name = 'AI-Powered Recommendation Engine'), (SELECT id FROM users WHERE user_name = 'daniel_manager'), 'ADMIN', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'AI-Powered Recommendation Engine'), (SELECT id FROM users WHERE user_name = 'alice_dev'), 'MEMBER', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'AI-Powered Recommendation Engine'), (SELECT id FROM users WHERE user_name = 'jack_dev'), 'MEMBER', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'AI-Powered Recommendation Engine'), (SELECT id FROM users WHERE user_name = 'isabel_dev'), 'MEMBER', CURRENT_TIMESTAMP);

-- Supply Chain Optimization Team
INSERT INTO project_members (project_id, user_id, role, joined_at)
VALUES
    ((SELECT id FROM projects WHERE name = 'Supply Chain Optimization'), (SELECT id FROM users WHERE user_name = 'laura_manager'), 'ADMIN', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Supply Chain Optimization'), (SELECT id FROM users WHERE user_name = 'bob_dev'), 'MEMBER', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Supply Chain Optimization'), (SELECT id FROM users WHERE user_name = 'frank_dev'), 'MEMBER', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Supply Chain Optimization'), (SELECT id FROM users WHERE user_name = 'david_qa'), 'MEMBER', CURRENT_TIMESTAMP);

-- Social Media Analytics Platform Team
INSERT INTO project_members (project_id, user_id, role, joined_at)
VALUES
    ((SELECT id FROM projects WHERE name = 'Social Media Analytics Platform'), (SELECT id FROM users WHERE user_name = 'kevin_manager'), 'ADMIN', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Social Media Analytics Platform'), (SELECT id FROM users WHERE user_name = 'emma_dev'), 'MEMBER', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Social Media Analytics Platform'), (SELECT id FROM users WHERE user_name = 'carol_designer'), 'MEMBER', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Social Media Analytics Platform'), (SELECT id FROM users WHERE user_name = 'liam_qa'), 'MEMBER', CURRENT_TIMESTAMP);

-- IoT Device Management Team
INSERT INTO project_members (project_id, user_id, role, joined_at)
VALUES
    ((SELECT id FROM projects WHERE name = 'IoT Device Management'), (SELECT id FROM users WHERE user_name = 'michelle_manager'), 'ADMIN', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'IoT Device Management'), (SELECT id FROM users WHERE user_name = 'jack_dev'), 'MEMBER', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'IoT Device Management'), (SELECT id FROM users WHERE user_name = 'alice_dev'), 'MEMBER', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'IoT Device Management'), (SELECT id FROM users WHERE user_name = 'henry_qa'), 'MEMBER', CURRENT_TIMESTAMP);

-- Learning Management System Team
INSERT INTO project_members (project_id, user_id, role, joined_at)
VALUES
    ((SELECT id FROM projects WHERE name = 'Learning Management System'), (SELECT id FROM users WHERE user_name = 'sarah_manager'), 'ADMIN', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Learning Management System'), (SELECT id FROM users WHERE user_name = 'grace_designer'), 'MEMBER', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Learning Management System'), (SELECT id FROM users WHERE user_name = 'kate_designer'), 'MEMBER', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Learning Management System'), (SELECT id FROM users WHERE user_name = 'isabel_dev'), 'MEMBER', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Learning Management System'), (SELECT id FROM users WHERE user_name = 'bob_dev'), 'MEMBER', CURRENT_TIMESTAMP);

-- Data Analytics Dashboard Team
INSERT INTO project_members (project_id, user_id, role, joined_at)
VALUES
    ((SELECT id FROM projects WHERE name = 'Data Analytics Dashboard'), (SELECT id FROM users WHERE user_name = 'mike_manager'), 'ADMIN', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Data Analytics Dashboard'), (SELECT id FROM users WHERE user_name = 'emma_dev'), 'MEMBER', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Data Analytics Dashboard'), (SELECT id FROM users WHERE user_name = 'jack_dev'), 'MEMBER', CURRENT_TIMESTAMP);

-- API Gateway Implementation Team
INSERT INTO project_members (project_id, user_id, role, joined_at)
VALUES
    ((SELECT id FROM projects WHERE name = 'API Gateway Implementation'), (SELECT id FROM users WHERE user_name = 'john_admin'), 'ADMIN', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'API Gateway Implementation'), (SELECT id FROM users WHERE user_name = 'frank_dev'), 'MEMBER', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'API Gateway Implementation'), (SELECT id FROM users WHERE user_name = 'alice_dev'), 'MEMBER', CURRENT_TIMESTAMP);

-- Cloud Migration Strategy Team
INSERT INTO project_members (project_id, user_id, role, joined_at)
VALUES
    ((SELECT id FROM projects WHERE name = 'Cloud Migration Strategy'), (SELECT id FROM users WHERE user_name = 'jennifer_admin'), 'ADMIN', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Cloud Migration Strategy'), (SELECT id FROM users WHERE user_name = 'sarah_manager'), 'ADMIN', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Cloud Migration Strategy'), (SELECT id FROM users WHERE user_name = 'bob_dev'), 'MEMBER', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Cloud Migration Strategy'), (SELECT id FROM users WHERE user_name = 'frank_dev'), 'MEMBER', CURRENT_TIMESTAMP);

-- Employee Self-Service Portal Team
INSERT INTO project_members (project_id, user_id, role, joined_at)
VALUES
    ((SELECT id FROM projects WHERE name = 'Employee Self-Service Portal'), (SELECT id FROM users WHERE user_name = 'laura_manager'), 'ADMIN', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Employee Self-Service Portal'), (SELECT id FROM users WHERE user_name = 'carol_designer'), 'MEMBER', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Employee Self-Service Portal'), (SELECT id FROM users WHERE user_name = 'isabel_dev'), 'MEMBER', CURRENT_TIMESTAMP);

-- Cybersecurity Enhancement Team
INSERT INTO project_members (project_id, user_id, role, joined_at)
VALUES
    ((SELECT id FROM projects WHERE name = 'Cybersecurity Enhancement'), (SELECT id FROM users WHERE user_name = 'admin'), 'ADMIN', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Cybersecurity Enhancement'), (SELECT id FROM users WHERE user_name = 'john_admin'), 'ADMIN', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Cybersecurity Enhancement'), (SELECT id FROM users WHERE user_name = 'alice_dev'), 'MEMBER', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Cybersecurity Enhancement'), (SELECT id FROM users WHERE user_name = 'henry_qa'), 'MEMBER', CURRENT_TIMESTAMP);

-- Multi-Tenant SaaS Platform Team
INSERT INTO project_members (project_id, user_id, role, joined_at)
VALUES
    ((SELECT id FROM projects WHERE name = 'Multi-Tenant SaaS Platform'), (SELECT id FROM users WHERE user_name = 'kevin_manager'), 'ADMIN', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Multi-Tenant SaaS Platform'), (SELECT id FROM users WHERE user_name = 'emma_dev'), 'MEMBER', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Multi-Tenant SaaS Platform'), (SELECT id FROM users WHERE user_name = 'jack_dev'), 'MEMBER', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Multi-Tenant SaaS Platform'), (SELECT id FROM users WHERE user_name = 'liam_qa'), 'MEMBER', CURRENT_TIMESTAMP);

-- Legacy System Migration Team
INSERT INTO project_members (project_id, user_id, role, joined_at)
VALUES
    ((SELECT id FROM projects WHERE name = 'Legacy System Migration'), (SELECT id FROM users WHERE user_name = 'alice_dev'), 'MEMBER', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Legacy System Migration'), (SELECT id FROM users WHERE user_name = 'bob_dev'), 'MEMBER', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Legacy System Migration'), (SELECT id FROM users WHERE user_name = 'david_qa'), 'MEMBER', CURRENT_TIMESTAMP);

-- Warehouse Automation Team
INSERT INTO project_members (project_id, user_id, role, joined_at)
VALUES
    ((SELECT id FROM projects WHERE name = 'Warehouse Automation'), (SELECT id FROM users WHERE user_name = 'emily_manager'), 'ADMIN', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Warehouse Automation'), (SELECT id FROM users WHERE user_name = 'frank_dev'), 'MEMBER', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Warehouse Automation'), (SELECT id FROM users WHERE user_name = 'jack_dev'), 'MEMBER', CURRENT_TIMESTAMP);

-- Virtual Reality Training Team
INSERT INTO project_members (project_id, user_id, role, joined_at)
VALUES
    ((SELECT id FROM projects WHERE name = 'Virtual Reality Training'), (SELECT id FROM users WHERE user_name = 'james_manager'), 'ADMIN', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Virtual Reality Training'), (SELECT id FROM users WHERE user_name = 'grace_designer'), 'MEMBER', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Virtual Reality Training'), (SELECT id FROM users WHERE user_name = 'kate_designer'), 'MEMBER', CURRENT_TIMESTAMP);

-- Authentication Service Team
INSERT INTO project_members (project_id, user_id, role, joined_at)
VALUES
    ((SELECT id FROM projects WHERE name = 'Authentication Service'), (SELECT id FROM users WHERE user_name = 'alice_dev'), 'MEMBER', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Authentication Service'), (SELECT id FROM users WHERE user_name = 'emma_dev'), 'MEMBER', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Authentication Service'), (SELECT id FROM users WHERE user_name = 'frank_dev'), 'MEMBER', CURRENT_TIMESTAMP);

-- Payment Gateway Integration Team
INSERT INTO project_members (project_id, user_id, role, joined_at)
VALUES
    ((SELECT id FROM projects WHERE name = 'Payment Gateway Integration'), (SELECT id FROM users WHERE user_name = 'mike_manager'), 'ADMIN', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Payment Gateway Integration'), (SELECT id FROM users WHERE user_name = 'bob_dev'), 'MEMBER', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Payment Gateway Integration'), (SELECT id FROM users WHERE user_name = 'david_qa'), 'MEMBER', CURRENT_TIMESTAMP);

-- Email Notification System Team
INSERT INTO project_members (project_id, user_id, role, joined_at)
VALUES
    ((SELECT id FROM projects WHERE name = 'Email Notification System'), (SELECT id FROM users WHERE user_name = 'michelle_manager'), 'ADMIN', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Email Notification System'), (SELECT id FROM users WHERE user_name = 'isabel_dev'), 'MEMBER', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Email Notification System'), (SELECT id FROM users WHERE user_name = 'jack_dev'), 'MEMBER', CURRENT_TIMESTAMP);

-- Performance Optimization Phase 1 Team
INSERT INTO project_members (project_id, user_id, role, joined_at)
VALUES
    ((SELECT id FROM projects WHERE name = 'Performance Optimization Phase 1'), (SELECT id FROM users WHERE user_name = 'daniel_manager'), 'ADMIN', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Performance Optimization Phase 1'), (SELECT id FROM users WHERE user_name = 'alice_dev'), 'MEMBER', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Performance Optimization Phase 1'), (SELECT id FROM users WHERE user_name = 'emma_dev'), 'MEMBER', CURRENT_TIMESTAMP);

-- Blockchain POC Team
INSERT INTO project_members (project_id, user_id, role, joined_at)
VALUES
    ((SELECT id FROM projects WHERE name = 'Blockchain POC'), (SELECT id FROM users WHERE user_name = 'john_admin'), 'ADMIN', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Blockchain POC'), (SELECT id FROM users WHERE user_name = 'frank_dev'), 'MEMBER', CURRENT_TIMESTAMP);

-- Chatbot Implementation Team
INSERT INTO project_members (project_id, user_id, role, joined_at)
VALUES
    ((SELECT id FROM projects WHERE name = 'Chatbot Implementation'), (SELECT id FROM users WHERE user_name = 'lisa_admin'), 'ADMIN', CURRENT_TIMESTAMP),
    ((SELECT id FROM projects WHERE name = 'Chatbot Implementation'), (SELECT id FROM users WHERE user_name = 'bob_dev'), 'MEMBER', CURRENT_TIMESTAMP);

-- ============================================================================
-- SEED DATA SUMMARY
-- ============================================================================
-- Users: 25 (5 ADMIN, 8 MANAGER, 12 MEMBER)
-- Projects: 25 (10 ACTIVE, 6 PLANNING, 3 ON_HOLD, 4 COMPLETED, 2 ABANDONED)
-- Project Members: 90+ memberships across all projects
-- Default Password: password123 (for all users)
-- ============================================================================
