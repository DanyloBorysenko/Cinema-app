package cinema.service.impl;

import static cinema.model.Role.RoleName.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import java.util.Set;

import cinema.exception.InputDataFormatException;
import cinema.model.Role;
import cinema.model.User;
import cinema.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
class CustomUserDetailsServiceTest {
    private UserService userServiceMock;
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        userServiceMock = Mockito.mock(UserService.class);
        customUserDetailsService = new CustomUserDetailsService(userServiceMock);
    }

    @Test
    void loadUserByUsername_Ok() {
        String email = "user@gmail.com";
        String password = "password";
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setId(1L);
        user.setRoles(Set.of(new Role(USER)));
        Mockito.when(userServiceMock.findByEmail(email)).thenReturn(Optional.of(user));
        String[] authorities = user.getRoles().stream()
                .map(role -> role.getName().name())
                .toArray(String[]::new);
        UserDetails expected = org.springframework.security.core.userdetails.User
                .withUsername(email)
                .password(password)
                .authorities(authorities)
                .build();
        UserDetails actual = customUserDetailsService.loadUserByUsername(email);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void loadUserByUsername_InvalidUserName_NotOk() {
        RuntimeException exception = assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("invalidUserName");
        });
        assertEquals("User with email: invalidUserName not found", exception.getMessage());
    }

    @Test
    void loadUserByUsername_EmailIsNull_NotOk() {
        assertThrows(InputDataFormatException.class, () -> {
            customUserDetailsService.loadUserByUsername(null);
        });
    }

}