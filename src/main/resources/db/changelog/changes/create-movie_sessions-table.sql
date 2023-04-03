CREATE TABLE `movie_sessions` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `movie_id` BIGINT NOT NULL,
    `cinema_hall_id` BIGINT NOT NULL,
    `show_time` DATETIME(0),
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_movie_sessions_movie_id` FOREIGN KEY (`movie_id`) REFERENCES `movies` (`id`),
    CONSTRAINT `fk_movie_sessions_cinema_hall_id` FOREIGN KEY (`cinema_hall_id`) REFERENCES `cinema_halls` (`id`)
);