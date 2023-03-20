package cinema.service;

import static cinema.model.Role.RoleName.ADMIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import cinema.dao.RoleDao;
import cinema.exception.InputDataFormatException;
import cinema.model.Role;
import cinema.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceTest {
    private RoleDao roleDaoMock;
    private RoleService roleService;
    Role role;

    @BeforeEach
    void setUp() {
        roleDaoMock = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDaoMock);
        role = new Role(ADMIN);
        role.setId(1L);
    }

    @Test
    void add_Ok() {
        Mockito.when(roleDaoMock.add(role)).thenReturn(role);
        Role actual = roleService.add(role);
        assertNotNull(actual);
        assertEquals(role, actual);
    }

    @Test
    void add_RoleIsNull_NotOk() {
        assertThrows(InputDataFormatException.class, () -> {
            roleService.add(null);
        });
    }

    @Test
    void getByName() {
        Mockito.when(roleDaoMock.getByName(role.getRoleName().name())).thenReturn(Optional.of(role));
        Role actual = roleService.getByName(role.getRoleName().name());
        assertNotNull(actual);
        assertEquals(role, actual);
    }

    @Test
    void getByName_RoleNameIsNull_NotOk() {
        assertThrows(InputDataFormatException.class, () -> {
            roleService.getByName(null);
        });
    }
}