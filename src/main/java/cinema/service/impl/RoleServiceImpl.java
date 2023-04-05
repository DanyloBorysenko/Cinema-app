package cinema.service.impl;

import cinema.dao.RoleDao;
import cinema.exception.InputDataFormatException;
import cinema.model.Role;
import cinema.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleDao roleDao;

    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public Role add(Role role) {
        if (role != null) {
            return roleDao.add(role);
        }
        throw new InputDataFormatException();
    }

    @Override
    public Role getByName(String roleName) {
        if (roleName == null) {
            throw new InputDataFormatException();
        }
        return roleDao.getByName(roleName).orElseThrow(
                () -> new RuntimeException("Role with role name " + roleName + " not found"));
    }

    @Override
    public Role getById(Long id) {
        return roleDao.getById(id);
    }
}
