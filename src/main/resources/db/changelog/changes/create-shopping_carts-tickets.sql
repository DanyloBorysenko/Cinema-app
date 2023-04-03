CREATE TABLE `shopping_carts_tickets`(
    `shopping_cart_id` BIGINT NOT NULL,
    `ticket_id` BIGINT NOT NULL,
    PRIMARY KEY (`shopping_cart_id`, `ticket_id`),
    CONSTRAINT `shopping_carts_tickets_shopping_cart_id` FOREIGN KEY (`shopping_cart_id`) REFERENCES `shopping_carts` (`id`),
    CONSTRAINT `shopping_carts_tickets_ticket_id` FOREIGN KEY (`ticket_id`) REFERENCES `tickets` (`id`)
);