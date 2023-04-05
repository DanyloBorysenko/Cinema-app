--liquibase formatted sql
--changeset danylborysenko:create-roles-table splitStatements:true endDelimiter:;
CREATE TABLE `roles` (
                        `id` BIGINT NOT NUll AUTO_INCREMENT ,
                        `name` VARCHAR(255) DEFAULT NULL,
                         PRIMARY KEY (`id`)
);

INSERT INTO `roles` VALUES (1, 'USER');
INSERT INTO `roles` VALUES (2, 'ADMIN');

--rollback DROP TABLE roles;