-- ===========================================================
-- E-book Management System - Database setup (MySQL)
-- ===========================================================
-- You normally do NOT need to run this manually because
-- application.properties uses createDatabaseIfNotExist=true
-- and Hibernate (ddl-auto=update) creates the tables for you.
-- This script is provided for reference / manual setup.

CREATE DATABASE IF NOT EXISTS ebook_management_system
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE ebook_management_system;

-- Tables (users, categories, books, collection_items) are
-- generated automatically by JPA/Hibernate on first run.
-- A seed admin and default categories are inserted by DataInitializer.

-- Default admin login (created automatically on first run):
--   email:    admin@ebook.com
--   password: admin123
