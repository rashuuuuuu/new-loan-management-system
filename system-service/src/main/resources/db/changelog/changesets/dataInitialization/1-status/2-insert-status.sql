-- liquibase formatted sql
--changeset rashmita.subedi:1

--preconditions onFail:CONTINUE onError:HALT
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM status
INSERT INTO status (description, icon, name,version)
VALUES
    ('CREATED','created','CREATED',0),
    ('UPDATED','updated','UPDATED',0),
    ('ACTIVE', 'active', 'ACTIVE',0),
    ('DELETED', 'deleted', 'DELETED',0),
    ('PENDING', 'pending', 'PENDING',0),
    ('BLOCKED', 'blocked', 'BLOCKED',0),
    ('UNBLOCKED', 'unblocked', 'UNBLOCKED',0);
