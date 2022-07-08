--liquibase formatted sql

-- Initial migration create the first tables
-- changeset cwininger:initial-migration-create-first-tables splitStatements:false splitRollbackStatements:false

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS reports (
    id uuid DEFAULT uuid_generate_v4(),
    cyclone_dx varchar(1048576) NOT NULL,
    created_at timestamp DEFAULT now()
);

-- rollback DROP TABLE Reports;
