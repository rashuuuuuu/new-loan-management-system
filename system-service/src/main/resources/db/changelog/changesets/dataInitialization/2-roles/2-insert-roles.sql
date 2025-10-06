-- liquibase formatted sql
--changeset rashmita.subedi:1

--preconditions onFail:CONTINUE onError:HALT
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM roles
UPDATE roles AS r
    INNER JOIN roles AS parent ON r.parent_name = parent.name
    SET r.parent_role = parent.id;
--changeset rashmita.subedi:2
INSERT INTO roles (description, icon, name, navigation, parent_name, permission, position, ui_group_name,version)
VALUES ('Root', '', 'Root', 'NONE', 'ROOT', 'NONE', 0, 'NONE',0),
       ('Admin Users', 'GrUserAdmin', 'Admin Users', '/adminUser', 'Root', 'ADMIN_USER', 1, 'NONE',0),
       ('Create Admin User', '', 'Create Admin User', 'create', 'Admin Users', 'CREATE_ADMIN_USER', 1, 'Admin Users',0),
       ('Modify Admin User', 'msi-edit', 'Modify Admin User', 'edit', 'Admin Users', 'MODIFY_ADMIN_USER', 2,
        'Admin Users',0),
       ('View Admin User', 'msi-view', 'View Admin User', 'view', 'Admin Users', 'VIEW_ADMIN_USER', 3, 'Admin Users',0),
       ('Delete Admin User', '', 'Delete Admin User', 'delete', 'Admin Users', 'DELETE_ADMIN_USER', 4, 'Admin Users',0),
       ('Block Admin User', '', 'Block Admin User', 'block', 'Admin Users', 'BLOCK_ADMIN_USER', 5, 'Admin Users',0),
       ('UnBlock Admin User', '', 'UnBlock Admin User', 'unblock', 'Admin Users', 'UNBLOCK_ADMIN_USER', 6,
        'Admin Users',0),
       ('Reopen Admin User', '', 'Reopen Admin User', 'reopen', 'Admin Users', 'REOPEN_ADMIN_USER', 7, 'Admin Users',0),
       ('Unlock Admin User', '', 'Unlock Admin User', 'unlock', 'Admin Users', 'UNLOCK_ADMIN_USER', 8, 'Admin Users',0),
       ('Reset Password Admin User', '', 'Reset Password Admin User', 'reset', 'Admin Users',
        'RESET_PASSWORD_ADMIN_USER', 9, 'Admin Users',0),
       ('Resend Account Activation Link Admin User', '', 'Resend Account Activation Link Admin User', 'resend',
        'Admin Users', 'RESEND_ACCOUNT_ACTIVATION_LINK_ADMIN_USER', 10, 'Admin Users',0),
       ('Reset Two Factor Authentication Admin User', '', 'Reset Two Factor Authentication Admin User',
        'resetTwoFactorAuth', 'Admin Users', 'RESET_TWO_FACTOR_AUTHENTICATION_ADMIN_USER', 11, 'Admin Users',0),
       ('Customers', 'BsPeople', 'Customers', '/customers', 'Root', 'CUSTOMERS', 2, 'NONE',0);

--changeset rashmita.subedi:3
--preconditions onFail:CONTINUE onError:HALT
--precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM roles WHERE name='Customers' AND parent_name='Root'

UPDATE roles set name='Users', description='Users', navigation='/users', permission='USERS' WHERE name='Customer' AND parent_name='Root';

--changeset rashmita.subedi:4
--preconditions onFail:CONTINUE onError:HALT
--precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM roles WHERE name='Customers' AND parent_name='Root'

UPDATE roles set name='Users', description='Users', navigation='/users', permission='USERS' WHERE name='Customers' AND parent_name='Root';

--changeset rashmita.subedi:5
--preconditions onFail:CONTINUE onError:HALT
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM roles WHERE name='Ward Information' AND parent_name='Services'
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM roles WHERE name='Municipal Authorities' AND parent_name='Services'
INSERT INTO roles (description, icon, name, navigation, parent_name, permission, position, ui_group_name,version)
    VALUES ('Ward Information', 'MdShareLocation', 'Ward Information', '/wardInformation', 'Services', 'WARD_INFORMATION', 11, 'Services',0),
           ('Municipal Authorities','BsFillPersonLinesFill','Municipal Authorities','/municipalAuthorities','Services','MUNICIPAL_AUTHORITIES',12,'Services',0);