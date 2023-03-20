package cinema.dao;

import static cinema.model.Role.RoleName.USER;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import cinema.AbstractTest;
import cinema.dao.impl.RoleDaoImpl;
import cinema.exception.DataProcessingException;
import cinema.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest {
    private RoleDao roleDao;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @BeforeEach
    void setUp() {
        role = new Role(USER);
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void add_Ok() {
        Role actual = roleDao.add(role);
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
    }

    @Test
    void add_RoleIsNull_NotOk() {
        assertThrows(DataProcessingException.class, () -> roleDao.add(null));
    }

    @Test
    void getByNameOk() {
        roleDao.add(role);
        Optional<Role> actual = roleDao.getByName("USER");
        assertNotNull(actual);
        assertEquals(role, actual.get());
    }

    @Test
    void getByNameOk_InvalidName_Ok() {
        Optional<Role> actual = roleDao.getByName("USER");
        assertTrue(actual.isEmpty());
    }

    @Test
    void getByNameOk_NameIsNull_Ok() {
        assertThrows(DataProcessingException.class, () -> roleDao.getByName(null));
    }
}