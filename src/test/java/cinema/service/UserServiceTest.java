package cinema.service;

import static cinema.model.Role.RoleName.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import cinema.dao.UserDao;
import cinema.exception.InputDataFormatException;
import cinema.model.Role;
import cinema.model.User;
import cinema.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class UserServiceTest {
    private UserDao userDaoMock;
    private UserService userService;
    private User user;

    @BeforeEach
    void setUp() {
        userDaoMock = Mockito.mock(UserDao.class);
        userService = new UserServiceImpl(new BCryptPasswordEncoder(), userDaoMock);
        user = new User();
        user.setEmail("user@email.com");
        user.setId(1L);
        user.setPassword("password");
        user.setRoles(Set.of(new Role(USER)));
    }

    @Test
    void add_Ok() {
        Mockito.when(userDaoMock.add(user)).thenReturn(user);
        User actual = userService.add(user);
        assertNotNull(actual);
        assertEquals(user, actual);
    }

    @Test
    void add_UserIsNull_NotOk() {
        assertThrows(InputDataFormatException.class, () -> userService.add(null));
    }

    @Test
    void get_Ok() {
        Mockito.when(userDaoMock.get(1L)).thenReturn(Optional.of(user));
        User actual = userService.get(1L);
        assertNotNull(actual);
        assertEquals(user, actual);
    }

    @Test
    void get_InvalidId_NotOk() {
        assertThrows(NoSuchElementException.class, () -> userService.get(2L));
    }

    @Test
    void get_IdIsNull_NotOk() {
        assertThrows(InputDataFormatException.class, () -> userService.get(null));
    }

    @Test
    void findByEmail_Ok() {
        Mockito.when(userDaoMock.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findByEmail(user.getEmail());
        assertTrue(actual.isPresent());
        assertEquals(Optional.of(user), actual);
    }

    @Test
    void findByEmail_InvalidEmailNotOk() {
        Optional<User> actual = userService.findByEmail("email");
        assertTrue(actual.isEmpty());
    }

    @Test
    void findByEmail_EmailIsNull_NotOk() {
        assertThrows(InputDataFormatException.class, () -> userService.findByEmail(null));
    }
}