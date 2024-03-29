package cinema.service.impl;

import cinema.exception.AuthenticationException;
import cinema.model.Role;
import cinema.model.User;
import cinema.service.AuthenticationService;
import cinema.service.RoleService;
import cinema.service.ShoppingCartService;
import cinema.service.UserService;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private static final Long USER_ROLE_ID = 1L;
    private final UserService userService;
    private final ShoppingCartService shoppingCartService;
    private final RoleService roleService;

    public AuthenticationServiceImpl(UserService userService,
                                     ShoppingCartService shoppingCartService,
                                     RoleService roleService) {
        this.userService = userService;
        this.shoppingCartService = shoppingCartService;
        this.roleService = roleService;
    }

    @Override
    public User register(String email, String password) {
        if (email == null || password == null) {
            throw new AuthenticationException("Email or password can't be null");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        Role userRole = roleService.getById(USER_ROLE_ID);
        user.setRoles(Set.of(userRole));
        User addedUser = userService.add(user);
        shoppingCartService.registerNewShoppingCart(addedUser);
        return addedUser;
    }
}
