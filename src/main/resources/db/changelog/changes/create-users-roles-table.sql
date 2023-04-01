CREATE TABLE `users_roles` (
                         `user_id` BIGINT NOT NUll,
                         `role_id` BIGINT NOT NULL,
                         PRIMARY KEY (user_id, role_id),
                         CONSTRAINT `fk_users_roles_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
                         CONSTRAINT `fk_users_roles_role_id` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
);