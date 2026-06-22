-- Reference Tables (Dictionaries)

CREATE TABLE cycle_status (
    code VARCHAR(50) PRIMARY KEY,
    description VARCHAR(255)
);

CREATE TABLE ticket_type (
    code VARCHAR(50) PRIMARY KEY,
    description VARCHAR(255)
);

CREATE TABLE ticket_status (
    code VARCHAR(50) PRIMARY KEY,
    description VARCHAR(255)
);

CREATE TABLE bug_priority (
    code VARCHAR(50) PRIMARY KEY,
    description VARCHAR(255)
);

CREATE TABLE bug_source (
    code VARCHAR(50) PRIMARY KEY,
    description VARCHAR(255)
);

CREATE TABLE bug_status (
    code VARCHAR(50) PRIMARY KEY,
    description VARCHAR(255)
);

CREATE TABLE rn_view_type (
    code VARCHAR(50) PRIMARY KEY,
    description VARCHAR(255)
);

CREATE TABLE uat_status (
    code VARCHAR(50) PRIMARY KEY,
    description VARCHAR(255)
);

CREATE TABLE deployment_status (
    code VARCHAR(50) PRIMARY KEY,
    description VARCHAR(255)
);

-- Data Initialization for Reference Tables

INSERT INTO cycle_status (code) VALUES 
('PLANNED'), ('IMPLEMENTATION'), ('QA_TESTING'), ('UAT'), ('READY_FOR_PROD'), ('DEPLOYED'), ('CLOSED');

INSERT INTO ticket_type (code) VALUES 
('NEW_FEATURE'), ('ENHANCEMENT');

INSERT INTO ticket_status (code) VALUES 
('TO_DO'), ('IN_PROGRESS'), ('CODE_REVIEW'), ('READY_FOR_QA'), ('QA_FAILED'), ('READY_FOR_UAT'), ('DONE');

INSERT INTO bug_priority (code) VALUES 
('LOW'), ('MEDIUM'), ('HIGH'), ('CRITICAL');

INSERT INTO bug_source (code) VALUES 
('STATIC_ANALYSIS'), ('CODE_REVIEW'), ('SQA'), ('UAT');

INSERT INTO bug_status (code) VALUES 
('NEW'), ('IN_PROGRESS'), ('VERIFIED');

INSERT INTO rn_view_type (code) VALUES 
('INTERNAL'), ('UAT'), ('PRODUCTION');

INSERT INTO uat_status (code) VALUES 
('PENDING'), ('IN_PROGRESS'), ('ACCEPTED'), ('REJECTED');

INSERT INTO deployment_status (code) VALUES 
('NOT_CREATED'), ('PLANNED'), ('STARTED'), ('SUCCESS'), ('CRITICAL_FAILURE');

-- Business Entities

-- 1. Release Cycle
CREATE TABLE release_cycle (
    cycle_id SERIAL PRIMARY KEY,
    target_version_tag VARCHAR(50) NOT NULL,
    start_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    planned_deployment_window TIMESTAMP,
    status_code VARCHAR(50) NOT NULL DEFAULT 'PLANNED' REFERENCES cycle_status(code)
);

-- 2. ITS Ticket
CREATE TABLE its_ticket (
    ticket_id VARCHAR(50) PRIMARY KEY,
    cycle_id INT REFERENCES release_cycle(cycle_id) ON DELETE SET NULL,
    title VARCHAR(255) NOT NULL,
    requirement_description TEXT,
    type_code VARCHAR(50) NOT NULL REFERENCES ticket_type(code),
    status_code VARCHAR(50) NOT NULL DEFAULT 'TO_DO' REFERENCES ticket_status(code),
    assignee_id BIGINT
);

-- 3. Bug Ticket (Rework)
CREATE TABLE bug_ticket (
    bug_id VARCHAR(50) PRIMARY KEY,
    parent_ticket_id VARCHAR(50) NOT NULL REFERENCES its_ticket(ticket_id) ON DELETE CASCADE,
    defect_description TEXT NOT NULL,
    priority_code VARCHAR(50) NOT NULL REFERENCES bug_priority(code),
    source_code VARCHAR(50) NOT NULL REFERENCES bug_source(code),
    status_code VARCHAR(50) NOT NULL DEFAULT 'NEW' REFERENCES bug_status(code)
);

-- 4. Software Build
CREATE TABLE software_build (
    build_id SERIAL PRIMARY KEY,
    cycle_id INT NOT NULL REFERENCES release_cycle(cycle_id) ON DELETE CASCADE,
    commit_hash VARCHAR(40) NOT NULL,
    compilation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    static_analysis_passed BOOLEAN
);

-- 5. Release Notes
CREATE TABLE release_notes (
    rn_id SERIAL PRIMARY KEY,
    build_id INT NOT NULL REFERENCES software_build(build_id) ON DELETE CASCADE,
    view_type_code VARCHAR(50) NOT NULL REFERENCES rn_view_type(code),
    text_content TEXT,
    last_updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 6. Portal Deployment
CREATE TABLE portal_deployment (
    deployment_id SERIAL PRIMARY KEY,
    cycle_id INT UNIQUE NOT NULL REFERENCES release_cycle(cycle_id) ON DELETE CASCADE,
    uat_environment_url VARCHAR(255),
    uat_status_code VARCHAR(50) NOT NULL DEFAULT 'PENDING' REFERENCES uat_status(code),
    approved_prod_date TIMESTAMP,
    deployment_status_code VARCHAR(50) NOT NULL DEFAULT 'NOT_CREATED' REFERENCES deployment_status(code)
);

-- 7. UAT Verification
CREATE TABLE uat_verification (
    verification_id SERIAL PRIMARY KEY,
    deployment_id INT NOT NULL REFERENCES portal_deployment(deployment_id) ON DELETE CASCADE,
    requirements_met BOOLEAN NOT NULL,
    no_operational_impact BOOLEAN NOT NULL,
    security_audit_passed BOOLEAN NOT NULL,
    rejection_justification TEXT,
    verification_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);