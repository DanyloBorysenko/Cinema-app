CREATE TABLE `tickets` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `movie_session_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `tickets_movie_session_id` FOREIGN KEY (`movie_session_id`) REFERENCES `movie_sessions` (`id`),
    CONSTRAINT `tickets_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);