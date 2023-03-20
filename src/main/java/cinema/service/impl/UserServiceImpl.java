package cinema.service.impl;

import cinema.dao.UserDao;
import cinema.exception.InputDataFormatException;
import cinema.model.User;
import cinema.service.UserService;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final PasswordEncoder encoder;
    private final UserDao userDao;

    public UserServiceImpl(PasswordEncoder encoder, UserDao userDao) {
        this.encoder = encoder;
        this.userDao = userDao;
    }

    @Override
    public User add(User user) {
        if (user != null) {
            user.setPassword(encoder.encode(user.getPassword()));
            return userDao.add(user);
        }
        throw new InputDataFormatException();
    }

    @Override
    public User get(Long id) {
        if (id == null) {
            throw new InputDataFormatException();
        }
        return userDao.get(id).orElseThrow(
                () -> new NoSuchElementException("User with id " + id + " not found"));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        if (email != null) {
            return userDao.findByEmail(email);
        }
        throw new InputDataFormatException();
    }
}
