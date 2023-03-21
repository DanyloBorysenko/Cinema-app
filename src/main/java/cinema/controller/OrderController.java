package cinema.controller;

import cinema.dto.response.OrderResponseDto;
import cinema.mapper.ResponseDtoMapper;
import cinema.model.Order;
import cinema.model.ShoppingCart;
import cinema.model.User;
import cinema.service.OrderService;
import cinema.service.ShoppingCartService;
import cinema.service.UserService;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private static final Logger logger = LogManager.getLogger(OrderController.class);
    private final ShoppingCartService shoppingCartService;
    private final OrderService orderService;
    private final UserService userService;
    private final ResponseDtoMapper<OrderResponseDto, Order> orderResponseDtoMapper;

    public OrderController(ShoppingCartService shoppingCartService,
                           OrderService orderService,
                           UserService userService,
                           ResponseDtoMapper<OrderResponseDto, Order> orderResponseDtoMapper) {
        this.shoppingCartService = shoppingCartService;
        this.orderService = orderService;
        this.userService = userService;
        this.orderResponseDtoMapper = orderResponseDtoMapper;
    }

    @PostMapping("/complete")
    public OrderResponseDto completeOrder(Authentication auth) {
        logger.info("Method completeOrder was called. Params : name = {}", auth.getName());
        String email = auth.getName();
        Optional<User> userOptional = userService.findByEmail(email);
        if (userOptional.isPresent()) {
            ShoppingCart cart = shoppingCartService.getByUser(userOptional.get());
            return orderResponseDtoMapper.mapToDto(orderService.completeOrder(cart));
        }
        logger.error("User with email " + email + " not found");
        throw new RuntimeException("User with email " + email + " not found");
    }

    @GetMapping
    public List<OrderResponseDto> getOrderHistory(Authentication auth) {
        logger.info("Method getOrderHistory was called. Params : name = {}.", auth.getName());
        String email = auth.getName();
        Optional<User> userOptional = userService.findByEmail(email);
        if (userOptional.isPresent()) {
            return orderService.getOrdersHistory(userOptional.get())
                    .stream()
                    .map(orderResponseDtoMapper::mapToDto)
                    .toList();
        }
        logger.error("User with email " + email + " not found");
        throw new RuntimeException("User with email " + email + " not found");
    }
}
