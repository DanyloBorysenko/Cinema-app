package cinema.service;

import static cinema.model.Role.RoleName.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.util.Set;
import cinema.exception.AuthenticationException;
import cinema.model.Role;
import cinema.model.User;
import cinema.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class AuthenticationServiceTest {
    private UserService userServiceMock;
    private ShoppingCartService shoppingCartServiceMock;
    private RoleService roleServiceMock;
    private AuthenticationService authenticationService;
    private User user;
    private String email = "user@gmail.com";
    private String password = "password";

    @BeforeEach
    void setUp() {
        userServiceMock = Mockito.mock(UserService.class);
        shoppingCartServiceMock = Mockito.mock(ShoppingCartService.class);
        roleServiceMock = Mockito.mock(RoleService.class);
        authenticationService = new AuthenticationServiceImpl(userServiceMock,
                shoppingCartServiceMock, roleServiceMock);
    }

    @Test
    void register_Ok() {
        user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(Set.of(new Role(USER)));
        user.setId(1L);
        Mockito.when(roleServiceMock.getById(1L)).thenReturn(new Role(USER));
        Mockito.when(userServiceMock.add(any())).thenReturn(user);
        User actual = authenticationService.register(email, password);
        assertNotNull(actual);
        assertEquals(user, actual);
    }

    @Test
    void register_EmailIsNull_NotOk() {
        assertThrows(AuthenticationException.class, () -> {
            authenticationService.register(null, password);
        });
    }

    @Test
    void register_PasswordIsNull_NotOk() {
        assertThrows(AuthenticationException.class, () -> {
            authenticationService.register(email, null);
        });
    }
}