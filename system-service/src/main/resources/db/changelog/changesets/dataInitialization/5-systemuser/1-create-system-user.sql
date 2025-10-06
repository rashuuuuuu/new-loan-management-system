-- liquibase formatted sql
--changeset rashmita.subedi:1
--preconditions onFail:CONTINUE onError:HALT
CREATE TABLE IF NOT EXISTS `system_user`
(
    id                           BIGINT AUTO_INCREMENT NOT NULL,
    version                      BIGINT                NOT NULL,
    name                         VARCHAR(255)          NOT NULL,
    password                     VARCHAR(255)          NULL,
    username                     VARCHAR(255)          NOT NULL,
    is_active                    BIT(1)                NULL,
    email                        VARCHAR(255)          NOT NULL,
    address                      VARCHAR(255)          NULL,
    created_at                   datetime              NULL,
    updated_at                   datetime              NULL,
    status_id                    BIGINT                NOT NULL,
    access_group                 BIGINT                NOT NULL,
    is_super_admin               BIT(1)                NULL,
    CONSTRAINT pk_admin PRIMARY KEY (id)
    );


--changeset rashmita.subedi:2
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:0  SELECT COUNT(*) FROM information_schema.table_constraints WHERE constraint_schema = (SELECT DATABASE()) AND table_name = 'system_user' AND constraint_name = 'FK_ADMIN_ON_ACCESS_GROUP'
ALTER TABLE `system_user`
    ADD CONSTRAINT FK_ADMIN_ON_ACCESS_GROUP FOREIGN KEY (access_group) REFERENCES access_group (id);

-- changeset rashmita.subedi:3
-- preconditions onFail:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.columns WHERE table_name = 'system_user' AND column_name = 'code'
ALTER TABLE `system_user` ADD COLUMN code VARCHAR(255);



