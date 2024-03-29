package cinema.dao.impl;

import cinema.dao.AbstractDao;
import cinema.dao.RoleDao;
import cinema.exception.DataProcessingException;
import cinema.model.Role;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDaoImpl extends AbstractDao<Role> implements RoleDao {
    public RoleDaoImpl(SessionFactory factory) {
        super(factory, Role.class);
    }

    @Override
    public Optional<Role> getByName(String roleName) {
        try (Session session = factory.openSession()) {
            Query<Role> getByName = session.createQuery(
                    "FROM Role r WHERE r.name = : roleName", Role.class)
                    .setHint("org.hibernate.cacheable", true);
            getByName.setParameter("roleName", Role.RoleName.valueOf(roleName));
            return getByName.uniqueResultOptional();
        } catch (Exception e) {
            throw new DataProcessingException("Role with role name " + roleName + " not found", e);
        }
    }

    @Override
    public Role getById(Long id) {
        try (Session session = factory.openSession()) {
            return session.get(Role.class, id);
        } catch (Exception e) {
            throw new DataProcessingException("Can't get role by id:" + id, e);
        }
    }
}
