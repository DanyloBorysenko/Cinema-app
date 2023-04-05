CREATE TABLE `orders_tickets` (
   `order_id` BIGINT NOT NULL,
   `ticket_id` BIGINT NOT NULL,
   PRIMARY KEY (`order_id`, `ticket_id`),
   CONSTRAINT `orders_tickets_order_id` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
   CONSTRAINT `orders_tickets_ticket_id` FOREIGN KEY (`ticket_id`) REFERENCES `tickets` (`id`)
);