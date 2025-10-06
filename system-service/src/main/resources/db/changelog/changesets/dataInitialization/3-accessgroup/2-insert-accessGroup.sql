-- liquibase formatted sql
--changeset rashmita.subedi:1

--preconditions onFail:CONTINUE onError:HALT
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM access_group
INSERT INTO access_group (name, description, created_at, status_id, is_super_admin_group, remarks,version)
VALUES
    ('Super Admin', 'System group with full access', now(), 1, true, 'Super admin group',0);

