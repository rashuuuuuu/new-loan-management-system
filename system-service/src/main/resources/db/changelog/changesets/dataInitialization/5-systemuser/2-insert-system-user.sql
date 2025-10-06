-- liquibase formatted sql
--changeset rashmita.subedi:1

--preconditions onFail:CONTINUE onError:HALT
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM system_user where username='superadmin'
INSERT INTO `system_user` (name, password, username, is_active, email, created_at,status_id, access_group, is_super_admin,version)
VALUES
    ('Super Admin', '$2a$10$h/Fm04H01xFqs1iZ8LEVPeg6YfEi/uRz1cLBI9i4KgoRKL0EHctsy', 'superadmin', true, 'rashmita@gmail.com', now(), 1, 1,   true,0);

-- changeset rashmita.subedi:2
-- preconditions onFail:CONTINUE onError:HALT
-- precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM information_schema.columns WHERE table_name = 'system_user' AND column_name = 'code'
UPDATE `system_user` SET code = uuid() WHERE code IS NULL;