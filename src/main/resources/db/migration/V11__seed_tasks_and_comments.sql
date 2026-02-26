-- ============================================================================
-- Seed Data: Tasks and Comments
-- Purpose: Populate tasks and comments for testing and UI development
-- Depends on: V8 (users, projects, members), V9 (tasks table), V10 (comments table)
-- ============================================================================

-- ============================================================================
-- TASKS (40 Tasks across 6 projects)
-- ============================================================================

-- ========================
-- E-Commerce Platform (10 tasks)
-- Owner: sarah_manager | Members: john_admin, alice_dev, bob_dev, carol_designer, david_qa, emma_dev
-- ========================

-- Epic
INSERT INTO tasks (title, description, status, priority, type, project_id, created_by_id, assigned_to_id, due_date, created_at, updated_at)
VALUES (
    'User Shopping Experience',
    'Epic covering all user-facing shopping features including product browsing, cart management, and checkout flow.',
    'IN_PROGRESS', 'HIGH', 'EPIC',
    (SELECT id FROM projects WHERE name = 'E-Commerce Platform'),
    (SELECT id FROM users WHERE user_name = 'sarah_manager'),
    (SELECT id FROM users WHERE user_name = 'sarah_manager'),
    '2025-04-30',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

-- Stories under the Epic
INSERT INTO tasks (title, description, status, priority, type, project_id, created_by_id, assigned_to_id, parent_task_id, due_date, created_at, updated_at)
VALUES (
    'Implement product listing page',
    'Create a responsive product listing page with grid/list view toggle, sorting options, and pagination. Should support filtering by category, price range, and rating.',
    'DONE', 'HIGH', 'STORY',
    (SELECT id FROM projects WHERE name = 'E-Commerce Platform'),
    (SELECT id FROM users WHERE user_name = 'sarah_manager'),
    (SELECT id FROM users WHERE user_name = 'alice_dev'),
    (SELECT id FROM tasks WHERE title = 'User Shopping Experience'),
    '2025-03-15',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

INSERT INTO tasks (title, description, status, priority, type, project_id, created_by_id, assigned_to_id, parent_task_id, due_date, created_at, updated_at)
VALUES (
    'Build shopping cart functionality',
    'Implement add to cart, update quantity, remove item, and cart total calculation. Cart should persist across sessions using localStorage.',
    'IN_PROGRESS', 'HIGH', 'STORY',
    (SELECT id FROM projects WHERE name = 'E-Commerce Platform'),
    (SELECT id FROM users WHERE user_name = 'sarah_manager'),
    (SELECT id FROM users WHERE user_name = 'bob_dev'),
    (SELECT id FROM tasks WHERE title = 'User Shopping Experience'),
    '2025-04-01',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

INSERT INTO tasks (title, description, status, priority, type, project_id, created_by_id, assigned_to_id, parent_task_id, due_date, created_at, updated_at)
VALUES (
    'Design checkout flow UI',
    'Create wireframes and implement the multi-step checkout flow: shipping address, payment method, order review, and confirmation.',
    'TODO', 'MEDIUM', 'STORY',
    (SELECT id FROM projects WHERE name = 'E-Commerce Platform'),
    (SELECT id FROM users WHERE user_name = 'sarah_manager'),
    (SELECT id FROM users WHERE user_name = 'carol_designer'),
    (SELECT id FROM tasks WHERE title = 'User Shopping Experience'),
    '2025-04-15',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

-- Standalone tasks
INSERT INTO tasks (title, description, status, priority, type, project_id, created_by_id, assigned_to_id, due_date, created_at, updated_at)
VALUES (
    'Fix product image loading on slow connections',
    'Product images take too long to load on 3G connections. Implement lazy loading and progressive image rendering with placeholder skeletons.',
    'TODO', 'MEDIUM', 'BUG',
    (SELECT id FROM projects WHERE name = 'E-Commerce Platform'),
    (SELECT id FROM users WHERE user_name = 'david_qa'),
    (SELECT id FROM users WHERE user_name = 'alice_dev'),
    '2025-03-20',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

INSERT INTO tasks (title, description, status, priority, type, project_id, created_by_id, assigned_to_id, due_date, created_at, updated_at)
VALUES (
    'Set up payment gateway integration',
    'Integrate Stripe payment gateway for handling credit card and debit card transactions. Include proper error handling and retry logic.',
    'BLOCKED', 'CRITICAL', 'TASK',
    (SELECT id FROM projects WHERE name = 'E-Commerce Platform'),
    (SELECT id FROM users WHERE user_name = 'sarah_manager'),
    (SELECT id FROM users WHERE user_name = 'emma_dev'),
    '2025-04-10',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

INSERT INTO tasks (title, description, status, priority, type, project_id, created_by_id, assigned_to_id, due_date, created_at, updated_at)
VALUES (
    'Write unit tests for cart service',
    'Add comprehensive unit tests for the shopping cart service layer. Cover edge cases: empty cart, max quantity, invalid products, concurrent modifications.',
    'TODO', 'LOW', 'TASK',
    (SELECT id FROM projects WHERE name = 'E-Commerce Platform'),
    (SELECT id FROM users WHERE user_name = 'john_admin'),
    (SELECT id FROM users WHERE user_name = 'david_qa'),
    '2025-04-20',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

INSERT INTO tasks (title, description, status, priority, type, project_id, created_by_id, assigned_to_id, due_date, created_at, updated_at)
VALUES (
    'Product search returns wrong results for special characters',
    'Searching for products with quotes or ampersands returns 500 error. Need to sanitize search input and fix the query builder.',
    'REVIEW', 'HIGH', 'DEFECT',
    (SELECT id FROM projects WHERE name = 'E-Commerce Platform'),
    (SELECT id FROM users WHERE user_name = 'david_qa'),
    (SELECT id FROM users WHERE user_name = 'bob_dev'),
    '2025-03-10',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

INSERT INTO tasks (title, description, status, priority, type, project_id, created_by_id, assigned_to_id, due_date, created_at, updated_at)
VALUES (
    'Implement product review system',
    'Allow authenticated users to leave reviews with star ratings (1-5) and text comments on products. Include moderation queue for admins.',
    'TODO', 'MEDIUM', 'STORY',
    (SELECT id FROM projects WHERE name = 'E-Commerce Platform'),
    (SELECT id FROM users WHERE user_name = 'sarah_manager'),
    NULL,
    '2025-05-15',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

INSERT INTO tasks (title, description, status, priority, type, project_id, created_by_id, due_date, created_at, updated_at)
VALUES (
    'Optimize database queries for product catalog',
    'Product listing API response time is 2.5s. Profile queries, add indexes, and implement caching to reduce to under 500ms.',
    'TODO', 'HIGH', 'TASK',
    (SELECT id FROM projects WHERE name = 'E-Commerce Platform'),
    (SELECT id FROM users WHERE user_name = 'john_admin'),
    '2025-04-25',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

-- ========================
-- Mobile Banking App (8 tasks)
-- Owner: mike_manager | Members: sarah_manager, frank_dev, grace_designer, henry_qa, isabel_dev
-- ========================

INSERT INTO tasks (title, description, status, priority, type, project_id, created_by_id, assigned_to_id, due_date, created_at, updated_at)
VALUES (
    'Account Management Module',
    'Epic for all account-related features: view balances, transaction history, account settings, and multi-account support.',
    'IN_PROGRESS', 'CRITICAL', 'EPIC',
    (SELECT id FROM projects WHERE name = 'Mobile Banking App'),
    (SELECT id FROM users WHERE user_name = 'mike_manager'),
    (SELECT id FROM users WHERE user_name = 'mike_manager'),
    '2025-06-30',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

INSERT INTO tasks (title, description, status, priority, type, project_id, created_by_id, assigned_to_id, due_date, created_at, updated_at)
VALUES (
    'Implement account balance dashboard',
    'Create dashboard showing all linked accounts with real-time balance updates. Include sparkline charts for recent balance trends.',
    'DONE', 'HIGH', 'STORY',
    (SELECT id FROM projects WHERE name = 'Mobile Banking App'),
    (SELECT id FROM users WHERE user_name = 'mike_manager'),
    (SELECT id FROM users WHERE user_name = 'frank_dev'),
    '2025-03-31',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

INSERT INTO tasks (title, description, status, priority, type, project_id, created_by_id, assigned_to_id, due_date, created_at, updated_at)
VALUES (
    'Build fund transfer flow',
    'Implement internal and external fund transfers with proper validation, OTP verification, and transaction confirmation screens.',
    'IN_PROGRESS', 'CRITICAL', 'STORY',
    (SELECT id FROM projects WHERE name = 'Mobile Banking App'),
    (SELECT id FROM users WHERE user_name = 'mike_manager'),
    (SELECT id FROM users WHERE user_name = 'isabel_dev'),
    '2025-04-15',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

INSERT INTO tasks (title, description, status, priority, type, project_id, created_by_id, assigned_to_id, due_date, created_at, updated_at)
VALUES (
    'Transaction history shows duplicate entries',
    'After performing a transfer, the transaction appears twice in history until page is refreshed. Likely a state management issue.',
    'TESTING', 'HIGH', 'BUG',
    (SELECT id FROM projects WHERE name = 'Mobile Banking App'),
    (SELECT id FROM users WHERE user_name = 'henry_qa'),
    (SELECT id FROM users WHERE user_name = 'frank_dev'),
    '2025-03-25',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

INSERT INTO tasks (title, description, status, priority, type, project_id, created_by_id, assigned_to_id, due_date, created_at, updated_at)
VALUES (
    'Design biometric authentication screens',
    'Create UI mockups for fingerprint and face ID authentication flows. Include fallback to PIN entry.',
    'DONE', 'MEDIUM', 'TASK',
    (SELECT id FROM projects WHERE name = 'Mobile Banking App'),
    (SELECT id FROM users WHERE user_name = 'mike_manager'),
    (SELECT id FROM users WHERE user_name = 'grace_designer'),
    '2025-03-10',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

INSERT INTO tasks (title, description, status, priority, type, project_id, created_by_id, assigned_to_id, due_date, created_at, updated_at)
VALUES (
    'Implement bill payment feature',
    'Allow users to pay utility bills, credit cards, and custom payees. Include scheduling recurring payments.',
    'TODO', 'MEDIUM', 'STORY',
    (SELECT id FROM projects WHERE name = 'Mobile Banking App'),
    (SELECT id FROM users WHERE user_name = 'sarah_manager'),
    (SELECT id FROM users WHERE user_name = 'isabel_dev'),
    '2025-05-31',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

INSERT INTO tasks (title, description, status, priority, type, project_id, created_by_id, assigned_to_id, due_date, created_at, updated_at)
VALUES (
    'Security audit for API endpoints',
    'Perform thorough security audit of all banking API endpoints. Check for SQL injection, XSS, CSRF, and improper authorization.',
    'TODO', 'CRITICAL', 'TASK',
    (SELECT id FROM projects WHERE name = 'Mobile Banking App'),
    (SELECT id FROM users WHERE user_name = 'mike_manager'),
    (SELECT id FROM users WHERE user_name = 'henry_qa'),
    '2025-04-30',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

INSERT INTO tasks (title, description, status, priority, type, project_id, created_by_id, due_date, created_at, updated_at)
VALUES (
    'Add push notification support',
    'Implement push notifications for transaction alerts, low balance warnings, and promotional messages. Support both iOS and Android.',
    'TODO', 'LOW', 'STORY',
    (SELECT id FROM projects WHERE name = 'Mobile Banking App'),
    (SELECT id FROM users WHERE user_name = 'mike_manager'),
    '2025-07-15',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

-- ========================
-- Healthcare Management System (7 tasks)
-- Owner: emily_manager | Members: john_admin, mike_manager, jack_dev, isabel_dev, liam_qa, grace_designer
-- ========================

INSERT INTO tasks (title, description, status, priority, type, project_id, created_by_id, assigned_to_id, due_date, created_at, updated_at)
VALUES (
    'Patient Records Management',
    'Epic covering electronic health records: patient registration, medical history, prescriptions, and lab results.',
    'IN_PROGRESS', 'CRITICAL', 'EPIC',
    (SELECT id FROM projects WHERE name = 'Healthcare Management System'),
    (SELECT id FROM users WHERE user_name = 'emily_manager'),
    (SELECT id FROM users WHERE user_name = 'emily_manager'),
    '2025-09-30',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

INSERT INTO tasks (title, description, status, priority, type, project_id, created_by_id, assigned_to_id, due_date, created_at, updated_at)
VALUES (
    'Build patient registration form',
    'Multi-step registration form with personal info, insurance details, emergency contacts, and medical history intake. HIPAA compliant.',
    'DONE', 'HIGH', 'STORY',
    (SELECT id FROM projects WHERE name = 'Healthcare Management System'),
    (SELECT id FROM users WHERE user_name = 'emily_manager'),
    (SELECT id FROM users WHERE user_name = 'jack_dev'),
    '2025-03-31',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

INSERT INTO tasks (title, description, status, priority, type, project_id, created_by_id, assigned_to_id, due_date, created_at, updated_at)
VALUES (
    'Implement appointment scheduling',
    'Calendar-based appointment scheduler with doctor availability, time slot selection, and conflict detection. Email/SMS reminders.',
    'IN_PROGRESS', 'HIGH', 'STORY',
    (SELECT id FROM projects WHERE name = 'Healthcare Management System'),
    (SELECT id FROM users WHERE user_name = 'emily_manager'),
    (SELECT id FROM users WHERE user_name = 'isabel_dev'),
    '2025-05-15',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

INSERT INTO tasks (title, description, status, priority, type, project_id, created_by_id, assigned_to_id, due_date, created_at, updated_at)
VALUES (
    'Patient search returns results from other departments',
    'When searching for patients in a specific department, results include patients from all departments. The department filter is being ignored.',
    'IN_PROGRESS', 'HIGH', 'BUG',
    (SELECT id FROM projects WHERE name = 'Healthcare Management System'),
    (SELECT id FROM users WHERE user_name = 'liam_qa'),
    (SELECT id FROM users WHERE user_name = 'jack_dev'),
    '2025-04-05',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

INSERT INTO tasks (title, description, status, priority, type, project_id, created_by_id, assigned_to_id, due_date, created_at, updated_at)
VALUES (
    'Design prescription management UI',
    'Create UI for doctors to write prescriptions, view drug interactions, and manage refill requests. Must follow medical standards.',
    'TODO', 'MEDIUM', 'TASK',
    (SELECT id FROM projects WHERE name = 'Healthcare Management System'),
    (SELECT id FROM users WHERE user_name = 'emily_manager'),
    (SELECT id FROM users WHERE user_name = 'grace_designer'),
    '2025-06-30',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

INSERT INTO tasks (title, description, status, priority, type, project_id, created_by_id, assigned_to_id, due_date, created_at, updated_at)
VALUES (
    'Implement HIPAA-compliant audit logging',
    'Add comprehensive audit logging for all patient data access. Track who viewed what, when, and from where. Required for HIPAA compliance.',
    'TODO', 'CRITICAL', 'TASK',
    (SELECT id FROM projects WHERE name = 'Healthcare Management System'),
    (SELECT id FROM users WHERE user_name = 'john_admin'),
    (SELECT id FROM users WHERE user_name = 'jack_dev'),
    '2025-05-31',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

INSERT INTO tasks (title, description, status, priority, type, project_id, created_by_id, due_date, created_at, updated_at)
VALUES (
    'Telemedicine video call integration',
    'Integrate WebRTC-based video calling for telemedicine appointments. Include screen sharing for reviewing lab results with patients.',
    'TODO', 'LOW', 'STORY',
    (SELECT id FROM projects WHERE name = 'Healthcare Management System'),
    (SELECT id FROM users WHERE user_name = 'emily_manager'),
    '2025-10-31',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

-- ========================
-- Real-Time Chat Application (5 tasks)
-- Owner: james_manager | Members: emily_manager, frank_dev, emma_dev, henry_qa
-- ========================

INSERT INTO tasks (title, description, status, priority, type, project_id, created_by_id, assigned_to_id, due_date, created_at, updated_at)
VALUES (
    'Implement WebSocket message handler',
    'Set up Spring WebSocket configuration with STOMP protocol. Handle message sending, receiving, and broadcasting to chat rooms.',
    'IN_PROGRESS', 'CRITICAL', 'STORY',
    (SELECT id FROM projects WHERE name = 'Real-Time Chat Application'),
    (SELECT id FROM users WHERE user_name = 'james_manager'),
    (SELECT id FROM users WHERE user_name = 'frank_dev'),
    '2025-04-15',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

INSERT INTO tasks (title, description, status, priority, type, project_id, created_by_id, assigned_to_id, due_date, created_at, updated_at)
VALUES (
    'Build chat room UI components',
    'Create message list, input area, typing indicators, and online user sidebar. Support emoji picker and message formatting.',
    'TODO', 'HIGH', 'STORY',
    (SELECT id FROM projects WHERE name = 'Real-Time Chat Application'),
    (SELECT id FROM users WHERE user_name = 'james_manager'),
    (SELECT id FROM users WHERE user_name = 'emma_dev'),
    '2025-05-01',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

INSERT INTO tasks (title, description, status, priority, type, project_id, created_by_id, assigned_to_id, due_date, created_at, updated_at)
VALUES (
    'Messages not delivered when user reconnects',
    'If a user loses connection briefly, messages sent during downtime are never delivered. Need offline message queue and sync on reconnect.',
    'TODO', 'HIGH', 'BUG',
    (SELECT id FROM projects WHERE name = 'Real-Time Chat Application'),
    (SELECT id FROM users WHERE user_name = 'henry_qa'),
    (SELECT id FROM users WHERE user_name = 'frank_dev'),
    '2025-04-20',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

INSERT INTO tasks (title, description, status, priority, type, project_id, created_by_id, assigned_to_id, due_date, created_at, updated_at)
VALUES (
    'Implement file sharing in chat',
    'Allow users to share files (images, documents, PDFs) in chat with previews. Max file size 25MB. Store in cloud storage.',
    'TODO', 'MEDIUM', 'STORY',
    (SELECT id FROM projects WHERE name = 'Real-Time Chat Application'),
    (SELECT id FROM users WHERE user_name = 'emily_manager'),
    NULL,
    '2025-06-15',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

INSERT INTO tasks (title, description, status, priority, type, project_id, created_by_id, assigned_to_id, due_date, created_at, updated_at)
VALUES (
    'Load testing for concurrent connections',
    'Performance test the WebSocket server with 10k concurrent connections. Identify bottlenecks and optimize message throughput.',
    'TODO', 'MEDIUM', 'TASK',
    (SELECT id FROM projects WHERE name = 'Real-Time Chat Application'),
    (SELECT id FROM users WHERE user_name = 'james_manager'),
    (SELECT id FROM users WHERE user_name = 'henry_qa'),
    '2025-06-30',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

-- ========================
-- Customer Portal Redesign (5 tasks)
-- Owner: john_admin | Members: sarah_manager, carol_designer, kate_designer, alice_dev, bob_dev
-- ========================

INSERT INTO tasks (title, description, status, priority, type, project_id, created_by_id, assigned_to_id, due_date, created_at, updated_at)
VALUES (
    'Redesign navigation structure',
    'Revamp the portal navigation based on user research. Implement mega menu, breadcrumbs, and contextual sidebar navigation.',
    'REVIEW', 'HIGH', 'STORY',
    (SELECT id FROM projects WHERE name = 'Customer Portal Redesign'),
    (SELECT id FROM users WHERE user_name = 'john_admin'),
    (SELECT id FROM users WHERE user_name = 'carol_designer'),
    '2025-03-20',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

INSERT INTO tasks (title, description, status, priority, type, project_id, created_by_id, assigned_to_id, due_date, created_at, updated_at)
VALUES (
    'Implement responsive design system',
    'Create a design system with reusable components, typography scale, color tokens, and spacing utilities. Mobile-first approach.',
    'IN_PROGRESS', 'HIGH', 'STORY',
    (SELECT id FROM projects WHERE name = 'Customer Portal Redesign'),
    (SELECT id FROM users WHERE user_name = 'john_admin'),
    (SELECT id FROM users WHERE user_name = 'kate_designer'),
    '2025-04-01',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

INSERT INTO tasks (title, description, status, priority, type, project_id, created_by_id, assigned_to_id, due_date, created_at, updated_at)
VALUES (
    'Migrate existing pages to new design',
    'Apply new design system to all existing portal pages. Ensure feature parity and fix any regressions.',
    'TODO', 'MEDIUM', 'TASK',
    (SELECT id FROM projects WHERE name = 'Customer Portal Redesign'),
    (SELECT id FROM users WHERE user_name = 'sarah_manager'),
    (SELECT id FROM users WHERE user_name = 'alice_dev'),
    '2025-04-15',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

INSERT INTO tasks (title, description, status, priority, type, project_id, created_by_id, assigned_to_id, due_date, created_at, updated_at)
VALUES (
    'Accessibility audit and fixes',
    'Run WCAG 2.1 AA compliance audit. Fix color contrast issues, add ARIA labels, keyboard navigation, and screen reader support.',
    'TODO', 'HIGH', 'TASK',
    (SELECT id FROM projects WHERE name = 'Customer Portal Redesign'),
    (SELECT id FROM users WHERE user_name = 'john_admin'),
    (SELECT id FROM users WHERE user_name = 'bob_dev'),
    '2025-04-20',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

INSERT INTO tasks (title, description, status, priority, type, project_id, created_by_id, assigned_to_id, due_date, created_at, updated_at)
VALUES (
    'CSS animations causing layout shift on Safari',
    'Page content jumps when animations trigger on Safari 17. The issue does not reproduce on Chrome or Firefox.',
    'IN_PROGRESS', 'MEDIUM', 'BUG',
    (SELECT id FROM projects WHERE name = 'Customer Portal Redesign'),
    (SELECT id FROM users WHERE user_name = 'bob_dev'),
    (SELECT id FROM users WHERE user_name = 'alice_dev'),
    '2025-03-25',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

-- ========================
-- Learning Management System (5 tasks)
-- Owner: jennifer_admin | Members: sarah_manager, grace_designer, kate_designer, isabel_dev, bob_dev
-- ========================

INSERT INTO tasks (title, description, status, priority, type, project_id, created_by_id, assigned_to_id, due_date, created_at, updated_at)
VALUES (
    'Course creation workflow',
    'Build instructor interface for creating courses: add modules, upload videos, create quizzes, and set prerequisites.',
    'IN_PROGRESS', 'HIGH', 'STORY',
    (SELECT id FROM projects WHERE name = 'Learning Management System'),
    (SELECT id FROM users WHERE user_name = 'jennifer_admin'),
    (SELECT id FROM users WHERE user_name = 'isabel_dev'),
    '2025-04-30',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

INSERT INTO tasks (title, description, status, priority, type, project_id, created_by_id, assigned_to_id, due_date, created_at, updated_at)
VALUES (
    'Student progress tracking dashboard',
    'Dashboard showing course completion percentage, quiz scores, time spent, and learning streaks. Include gamification badges.',
    'TODO', 'MEDIUM', 'STORY',
    (SELECT id FROM projects WHERE name = 'Learning Management System'),
    (SELECT id FROM users WHERE user_name = 'jennifer_admin'),
    (SELECT id FROM users WHERE user_name = 'bob_dev'),
    '2025-05-15',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

INSERT INTO tasks (title, description, status, priority, type, project_id, created_by_id, assigned_to_id, due_date, created_at, updated_at)
VALUES (
    'Video player buffering on mobile devices',
    'Video lectures buffer excessively on mobile. Implement adaptive bitrate streaming and preload next segment.',
    'TODO', 'HIGH', 'BUG',
    (SELECT id FROM projects WHERE name = 'Learning Management System'),
    (SELECT id FROM users WHERE user_name = 'sarah_manager'),
    (SELECT id FROM users WHERE user_name = 'isabel_dev'),
    '2025-04-10',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

INSERT INTO tasks (title, description, status, priority, type, project_id, created_by_id, assigned_to_id, due_date, created_at, updated_at)
VALUES (
    'Design certificate generation system',
    'Create customizable certificate templates that auto-populate with student name, course title, and completion date. PDF export.',
    'TODO', 'LOW', 'TASK',
    (SELECT id FROM projects WHERE name = 'Learning Management System'),
    (SELECT id FROM users WHERE user_name = 'jennifer_admin'),
    (SELECT id FROM users WHERE user_name = 'grace_designer'),
    '2025-05-31',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

INSERT INTO tasks (title, description, status, priority, type, project_id, created_by_id, due_date, created_at, updated_at)
VALUES (
    'Implement discussion forum per course',
    'Add threaded discussion forums for each course. Students and instructors can post questions, answers, and announcements.',
    'TODO', 'MEDIUM', 'STORY',
    (SELECT id FROM projects WHERE name = 'Learning Management System'),
    (SELECT id FROM users WHERE user_name = 'jennifer_admin'),
    '2025-06-15',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);


-- ============================================================================
-- COMMENTS (20 comments across several tasks)
-- ============================================================================

-- Comments on "Build shopping cart functionality" (E-Commerce Platform)
INSERT INTO comments (content, task_id, author_id, depth, created_at)
VALUES (
    'Should we use localStorage or a backend cart service for persistence? localStorage is simpler but we lose the cart if the user clears browser data.',
    (SELECT id FROM tasks WHERE title = 'Build shopping cart functionality'),
    (SELECT id FROM users WHERE user_name = 'bob_dev'),
    0, CURRENT_TIMESTAMP - INTERVAL '3 days'
);

INSERT INTO comments (content, task_id, author_id, parent_comment_id, depth, created_at)
VALUES (
    'I think we should use both - localStorage for guest users, and sync to backend when they log in. That way we get the best of both worlds.',
    (SELECT id FROM tasks WHERE title = 'Build shopping cart functionality'),
    (SELECT id FROM users WHERE user_name = 'alice_dev'),
    (SELECT id FROM comments WHERE content LIKE 'Should we use localStorage%'),
    1, CURRENT_TIMESTAMP - INTERVAL '3 days' + INTERVAL '2 hours'
);

INSERT INTO comments (content, task_id, author_id, parent_comment_id, depth, created_at)
VALUES (
    'Good idea Alice. Lets go with the hybrid approach. Bob, can you start with localStorage and well add the sync layer later?',
    (SELECT id FROM tasks WHERE title = 'Build shopping cart functionality'),
    (SELECT id FROM users WHERE user_name = 'sarah_manager'),
    (SELECT id FROM comments WHERE content LIKE 'Should we use localStorage%'),
    1, CURRENT_TIMESTAMP - INTERVAL '3 days' + INTERVAL '4 hours'
);

INSERT INTO comments (content, task_id, author_id, depth, created_at)
VALUES (
    'Quick update: cart add/remove is working. Currently implementing quantity validation - max 99 per item. Will push PR by end of day.',
    (SELECT id FROM tasks WHERE title = 'Build shopping cart functionality'),
    (SELECT id FROM users WHERE user_name = 'bob_dev'),
    0, CURRENT_TIMESTAMP - INTERVAL '1 day'
);

INSERT INTO comments (content, task_id, author_id, parent_comment_id, depth, created_at)
VALUES (
    'Nice progress! Make sure to handle the case where a product goes out of stock while its in someones cart.',
    (SELECT id FROM tasks WHERE title = 'Build shopping cart functionality'),
    (SELECT id FROM users WHERE user_name = 'david_qa'),
    (SELECT id FROM comments WHERE content LIKE 'Quick update: cart add/remove%'),
    1, CURRENT_TIMESTAMP - INTERVAL '1 day' + INTERVAL '1 hour'
);

-- Comments on "Set up payment gateway integration" (E-Commerce Platform)
INSERT INTO comments (content, task_id, author_id, depth, created_at)
VALUES (
    'This is currently blocked because we are waiting for Stripe API credentials from the finance team. I have raised a ticket with them.',
    (SELECT id FROM tasks WHERE title = 'Set up payment gateway integration'),
    (SELECT id FROM users WHERE user_name = 'emma_dev'),
    0, CURRENT_TIMESTAMP - INTERVAL '5 days'
);

INSERT INTO comments (content, task_id, author_id, parent_comment_id, depth, created_at)
VALUES (
    'I followed up with finance - they said credentials will be ready by next Monday. In the meantime, we can use Stripe test mode keys for development.',
    (SELECT id FROM tasks WHERE title = 'Set up payment gateway integration'),
    (SELECT id FROM users WHERE user_name = 'sarah_manager'),
    (SELECT id FROM comments WHERE content LIKE 'This is currently blocked%'),
    1, CURRENT_TIMESTAMP - INTERVAL '4 days'
);

-- Comments on "Build fund transfer flow" (Mobile Banking App)
INSERT INTO comments (content, task_id, author_id, depth, created_at)
VALUES (
    'For OTP verification, should we use SMS or email? SMS is more secure but costs more per message.',
    (SELECT id FROM tasks WHERE title = 'Build fund transfer flow'),
    (SELECT id FROM users WHERE user_name = 'isabel_dev'),
    0, CURRENT_TIMESTAMP - INTERVAL '2 days'
);

INSERT INTO comments (content, task_id, author_id, parent_comment_id, depth, created_at)
VALUES (
    'Lets support both and let the user choose their preferred method in settings. Start with email for now since its free, and add SMS in the next sprint.',
    (SELECT id FROM tasks WHERE title = 'Build fund transfer flow'),
    (SELECT id FROM users WHERE user_name = 'mike_manager'),
    (SELECT id FROM comments WHERE content LIKE 'For OTP verification%'),
    1, CURRENT_TIMESTAMP - INTERVAL '2 days' + INTERVAL '3 hours'
);

INSERT INTO comments (content, task_id, author_id, depth, created_at)
VALUES (
    'Important: Make sure all transfer amounts are validated server-side. Never trust client-side validation for financial transactions.',
    (SELECT id FROM tasks WHERE title = 'Build fund transfer flow'),
    (SELECT id FROM users WHERE user_name = 'henry_qa'),
    0, CURRENT_TIMESTAMP - INTERVAL '1 day'
);

-- Comments on "Implement appointment scheduling" (Healthcare Management System)
INSERT INTO comments (content, task_id, author_id, depth, created_at)
VALUES (
    'I have completed the calendar UI with FullCalendar library. Working on the time slot selection now. Question: should we support 15-min or 30-min slots?',
    (SELECT id FROM tasks WHERE title = 'Implement appointment scheduling'),
    (SELECT id FROM users WHERE user_name = 'isabel_dev'),
    0, CURRENT_TIMESTAMP - INTERVAL '4 days'
);

INSERT INTO comments (content, task_id, author_id, parent_comment_id, depth, created_at)
VALUES (
    'Make it configurable per doctor. Some specialists need 30-min slots while general practitioners can do 15-min consultations.',
    (SELECT id FROM tasks WHERE title = 'Implement appointment scheduling'),
    (SELECT id FROM users WHERE user_name = 'emily_manager'),
    (SELECT id FROM comments WHERE content LIKE 'I have completed the calendar%'),
    1, CURRENT_TIMESTAMP - INTERVAL '4 days' + INTERVAL '2 hours'
);

INSERT INTO comments (content, task_id, author_id, parent_comment_id, depth, created_at)
VALUES (
    'Agreed with Emily. Also consider buffer time between appointments for the doctor to write notes.',
    (SELECT id FROM tasks WHERE title = 'Implement appointment scheduling'),
    (SELECT id FROM users WHERE user_name = 'john_admin'),
    (SELECT id FROM comments WHERE content LIKE 'I have completed the calendar%'),
    1, CURRENT_TIMESTAMP - INTERVAL '4 days' + INTERVAL '5 hours'
);

-- Comments on "Implement WebSocket message handler" (Real-Time Chat Application)
INSERT INTO comments (content, task_id, author_id, depth, created_at)
VALUES (
    'Basic STOMP configuration is done. Messages are sending and receiving in real-time. Next: implementing chat rooms and message persistence.',
    (SELECT id FROM tasks WHERE title = 'Implement WebSocket message handler'),
    (SELECT id FROM users WHERE user_name = 'frank_dev'),
    0, CURRENT_TIMESTAMP - INTERVAL '6 days'
);

INSERT INTO comments (content, task_id, author_id, parent_comment_id, depth, created_at)
VALUES (
    'Great work! Before moving to chat rooms, can you add basic error handling for connection drops? We need graceful reconnection.',
    (SELECT id FROM tasks WHERE title = 'Implement WebSocket message handler'),
    (SELECT id FROM users WHERE user_name = 'james_manager'),
    (SELECT id FROM comments WHERE content LIKE 'Basic STOMP configuration%'),
    1, CURRENT_TIMESTAMP - INTERVAL '6 days' + INTERVAL '1 hour'
);

INSERT INTO comments (content, task_id, author_id, depth, created_at)
VALUES (
    'I noticed the message ordering can get mixed up under high load. We should add sequence numbers to messages to ensure correct ordering on the client side.',
    (SELECT id FROM tasks WHERE title = 'Implement WebSocket message handler'),
    (SELECT id FROM users WHERE user_name = 'emma_dev'),
    0, CURRENT_TIMESTAMP - INTERVAL '2 days'
);

-- Comments on "Redesign navigation structure" (Customer Portal Redesign)
INSERT INTO comments (content, task_id, author_id, depth, created_at)
VALUES (
    'Uploaded the navigation wireframes to Figma. Please review when you get a chance: the mega menu has 3 levels of nesting.',
    (SELECT id FROM tasks WHERE title = 'Redesign navigation structure'),
    (SELECT id FROM users WHERE user_name = 'carol_designer'),
    0, CURRENT_TIMESTAMP - INTERVAL '7 days'
);

INSERT INTO comments (content, task_id, author_id, parent_comment_id, depth, created_at)
VALUES (
    'Reviewed the wireframes. I think 3 levels is too deep for mobile - can we collapse to 2 levels on screens under 768px?',
    (SELECT id FROM tasks WHERE title = 'Redesign navigation structure'),
    (SELECT id FROM users WHERE user_name = 'alice_dev'),
    (SELECT id FROM comments WHERE content LIKE 'Uploaded the navigation wireframes%'),
    1, CURRENT_TIMESTAMP - INTERVAL '7 days' + INTERVAL '5 hours'
);

-- A soft-deleted comment example
INSERT INTO comments (content, task_id, author_id, depth, is_deleted, deleted_at, deleted_by, created_at)
VALUES (
    'This comment contained incorrect information about the API spec.',
    (SELECT id FROM tasks WHERE title = 'Redesign navigation structure'),
    (SELECT id FROM users WHERE user_name = 'bob_dev'),
    0, true, CURRENT_TIMESTAMP - INTERVAL '5 days',
    (SELECT id FROM users WHERE user_name = 'bob_dev'),
    CURRENT_TIMESTAMP - INTERVAL '6 days'
);

INSERT INTO comments (content, task_id, author_id, parent_comment_id, depth, created_at)
VALUES (
    'No worries Bob, thanks for the correction. Updated specs are in Confluence.',
    (SELECT id FROM tasks WHERE title = 'Redesign navigation structure'),
    (SELECT id FROM users WHERE user_name = 'john_admin'),
    (SELECT id FROM comments WHERE content LIKE 'This comment contained incorrect%'),
    1, CURRENT_TIMESTAMP - INTERVAL '5 days' + INTERVAL '2 hours'
);

-- ============================================================================
-- SEED DATA SUMMARY
-- ============================================================================
-- Tasks: 40 total
--   E-Commerce Platform:          10 (1 EPIC, 3 STORY, 2 TASK, 2 BUG, 1 DEFECT, 1 unassigned)
--   Mobile Banking App:            8 (1 EPIC, 3 STORY, 2 TASK, 1 BUG, 1 unassigned)
--   Healthcare Management System:  7 (1 EPIC, 2 STORY, 2 TASK, 1 BUG, 1 unassigned)
--   Real-Time Chat Application:    5 (2 STORY, 1 TASK, 1 BUG, 1 unassigned)
--   Customer Portal Redesign:      5 (2 STORY, 2 TASK, 1 BUG)
--   Learning Management System:    5 (3 STORY, 1 TASK, 1 BUG, 1 unassigned)
--
-- Comments: 20 total
--   Top-level (depth=0): 10
--   Replies (depth=1):   10
--   Soft-deleted:         1
--
-- Statuses covered: TODO, IN_PROGRESS, BLOCKED, REVIEW, TESTING, DONE
-- Priorities covered: LOW, MEDIUM, HIGH, CRITICAL
-- Types covered: EPIC, STORY, BUG, DEFECT, TASK
-- ============================================================================
