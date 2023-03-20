package cinema.dao;

import static cinema.model.Role.RoleName.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.Set;
import cinema.AbstractTest;
import cinema.dao.impl.RoleDaoImpl;
import cinema.dao.impl.UserDaoImpl;
import cinema.exception.DataProcessingException;
import cinema.model.Role;
import cinema.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoTest extends AbstractTest {
    private UserDao userDao;
    private User user;
    private RoleDao roleDao;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        role = new Role(USER);
        roleDao = new RoleDaoImpl(getSessionFactory());
        roleDao.add(role);
        user = new User();
        user.setEmail("email");
        user.setPassword("password");
        user.setRoles(Set.of(role));
        userDao = new UserDaoImpl(getSessionFactory());
    }

    @Test
    void add_Ok() {
        User actual = userDao.add(user);
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
    }

    @Test
    void add_UserIsNull_NotOk() {
        assertThrows(DataProcessingException.class, () -> userDao.add(null));
    }

    @Test
    void get_Ok() {
        userDao.add(user);
        Optional<User> optionalUser = userDao.get(1L);
        assertTrue(optionalUser.isPresent());
        assertEquals(user, optionalUser.get());
    }

    @Test
    void get_InvalidUser_Ok() {
        Optional<User> optionalUser = userDao.get(1L);
        assertTrue(optionalUser.isEmpty());
    }

    @Test
    void get_IdIsNull_NotOk() {
        assertThrows(DataProcessingException.class, () -> userDao.get(null));
    }

    @Test
    void findByEmail_Ok() {
        userDao.add(user);
        Optional<User> optionalUser = userDao.findByEmail(user.getEmail());
        assertTrue(optionalUser.isPresent());
        assertEquals(user, optionalUser.get());
    }

    @Test
    void findByEmail_InvalidUserEmail_Ok() {
        Optional<User> optionalUser = userDao.findByEmail("invalidEmail");
        assertTrue(optionalUser.isEmpty());
    }

    @Test
    void findByEmail_UserEmailIsNull_NotOk() {
        Optional<User> optionalUser = userDao.findByEmail(null);
        assertTrue(optionalUser.isEmpty());
    }
}
