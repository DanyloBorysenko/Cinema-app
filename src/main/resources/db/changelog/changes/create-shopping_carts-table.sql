CREATE TABLE `shopping_carts` (
    `id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `shopping_carts_id` FOREIGN KEY (`id`) REFERENCES `users` (`id`)
);