package cinema.controller;

import cinema.dto.response.ShoppingCartResponseDto;
import cinema.mapper.ResponseDtoMapper;
import cinema.model.MovieSession;
import cinema.model.ShoppingCart;
import cinema.model.User;
import cinema.service.MovieSessionService;
import cinema.service.ShoppingCartService;
import cinema.service.UserService;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shopping-carts")
public class ShoppingCartController {
    private static final Logger logger = LogManager.getLogger(ShoppingCartController.class);
    private final ShoppingCartService shoppingCartService;
    private final MovieSessionService movieSessionService;
    private final UserService userService;
    private final ResponseDtoMapper<ShoppingCartResponseDto, ShoppingCart>
            shoppingCartResponseDtoMapper;

    public ShoppingCartController(ShoppingCartService shoppingCartService,
                                  UserService userService,
                                  MovieSessionService movieSessionService,
            ResponseDtoMapper<ShoppingCartResponseDto, ShoppingCart>
                                      shoppingCartResponseDtoMapper) {
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
        this.movieSessionService = movieSessionService;
        this.shoppingCartResponseDtoMapper = shoppingCartResponseDtoMapper;
    }

    @PutMapping("/movie-sessions")
    public void addToCart(Authentication auth, @RequestParam Long movieSessionId) {
        logger.info("Method addToCart was called. Params : name = {}, movieSessionId = {}.",
                auth.getName(), movieSessionId);
        UserDetails details = (UserDetails) auth.getPrincipal();
        String email = details.getUsername();
        Optional<User> userOptional = userService.findByEmail(email);
        if (userOptional.isPresent()) {
            MovieSession movieSession = movieSessionService.get(movieSessionId);
            shoppingCartService.addSession(movieSession, userOptional.get());
        }
        logger.error("User with email " + email + " not found");
        throw new RuntimeException("User with email " + email + " not found");
    }

    @GetMapping("/by-user")
    public ShoppingCartResponseDto getByUser(Authentication auth) {
        logger.info("Method getByUser was called. Params: name = {}.", auth.getName());
        UserDetails details = (UserDetails) auth.getPrincipal();
        String email = details.getUsername();
        Optional<User> userOptional = userService.findByEmail(email);
        if (userOptional.isPresent()) {
            return shoppingCartResponseDtoMapper
                    .mapToDto(shoppingCartService.getByUser(userOptional.get()));
        }
        logger.error("User with email " + email + " not found");
        throw new RuntimeException("User with email " + email + " not found");
    }
}
