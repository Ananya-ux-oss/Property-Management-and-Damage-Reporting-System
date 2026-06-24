-- ════════════════════════════════════════════════════════════════════════════
-- schema.sql
-- Property Management and Damage Reporting System — MySQL Schema (reference)
-- ════════════════════════════════════════════════════════════════════════════
--
-- NOTE: You no longer need to run this file by hand. The Java program
-- (DatabaseConnection class) now creates the "property_management_db"
-- database and all three tables automatically the first time it runs,
-- using the exact same CREATE DATABASE / CREATE TABLE IF NOT EXISTS
-- statements shown below.
--
-- This file is kept as a reference — useful if you want to inspect the
-- exact schema, set it up ahead of time yourself, or recreate it manually
-- after dropping the database:
--
--   mysql -u root -p < schema.sql
--
-- To reset everything and let the Java program rebuild it from scratch:
--   DROP DATABASE property_management_db;
-- ════════════════════════════════════════════════════════════════════════════

CREATE DATABASE IF NOT EXISTS property_management_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE property_management_db;

-- ── users table ────────────────────────────────────────────────────────────
-- Stores Student, Manager, and Admin accounts (role column distinguishes them).
CREATE TABLE IF NOT EXISTS users (
    user_id       VARCHAR(20)  PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    email         VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role          VARCHAR(20)  NOT NULL,
    created_at    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_users_role CHECK (role IN ('Student', 'Manager', 'Admin'))
) ENGINE=InnoDB;

-- ── properties table ─────────────────────────────────────────────────────────
-- Stores every registered property (dorm room, lab, office, etc.).
-- NOTE: capacity column removed — not part of this version of the schema.
CREATE TABLE IF NOT EXISTS properties (
    property_id   VARCHAR(20)  PRIMARY KEY,
    property_name VARCHAR(150) NOT NULL,
    type          VARCHAR(50),
    location      VARCHAR(255) DEFAULT 'Unassigned',
    manager_id    VARCHAR(20),
    active        BOOLEAN      DEFAULT TRUE,
    registered_at TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_properties_manager FOREIGN KEY (manager_id) REFERENCES users(user_id)
) ENGINE=InnoDB;

-- ── damage_reports table ──────────────────────────────────────────────────────
-- Stores every damage report a Student files against a Property.
CREATE TABLE IF NOT EXISTS damage_reports (
    report_id    VARCHAR(30)  PRIMARY KEY,
    student_id   VARCHAR(20)  NOT NULL,
    property_id  VARCHAR(20)  NOT NULL,
    description  TEXT,
    status       VARCHAR(20)  DEFAULT 'PENDING',
    review_notes TEXT,
    reported_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_reports_student  FOREIGN KEY (student_id)  REFERENCES users(user_id),
    CONSTRAINT fk_reports_property FOREIGN KEY (property_id) REFERENCES properties(property_id),
    CONSTRAINT chk_reports_status  CHECK (status IN ('PENDING', 'REVIEWED', 'RESOLVED'))
) ENGINE=InnoDB;

-- ── helpful indexes ───────────────────────────────────────────────────────────
CREATE INDEX idx_properties_manager ON properties(manager_id);
CREATE INDEX idx_reports_property   ON damage_reports(property_id);
CREATE INDEX idx_reports_student    ON damage_reports(student_id);
