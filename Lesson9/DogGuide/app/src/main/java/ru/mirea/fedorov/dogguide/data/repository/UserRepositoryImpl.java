package ru.mirea.fedorov.dogguide.data.repository;

import java.util.Locale;

import ru.mirea.fedorov.dogguide.data.dao.UserDao;
import ru.mirea.fedorov.dogguide.data.model.UserEntity;
import ru.mirea.fedorov.dogguide.data.storage.PasswordHasher;
import ru.mirea.fedorov.dogguide.domain.models.User;
import ru.mirea.fedorov.dogguide.domain.repository.UserRepository;

public class UserRepositoryImpl implements UserRepository {
    private final UserDao userDao;
    private final PasswordHasher passwordHasher;
    private User currentUser;

    public UserRepositoryImpl(UserDao userDao, PasswordHasher passwordHasher) {
        this.userDao = userDao;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public User login(String login, String password) {
        String normalized = normalize(login);
        UserEntity stored = userDao.getByLogin(normalized);
        if (stored == null) {
            return null;
        }
        if (!stored.getPasswordHash().equals(passwordHasher.hash(password))) {
            return null;
        }
        currentUser = new User(normalized, normalized);
        return currentUser;
    }

    @Override
    public User register(String login, String password) {
        String normalized = normalize(login);
        if (userDao.getByLogin(normalized) != null) {
            return null;
        }
        userDao.insert(new UserEntity(normalized, passwordHasher.hash(password)));
        currentUser = new User(normalized, normalized);
        return currentUser;
    }

    @Override
    public void logout() {
        currentUser = null;
    }

    @Override
    public User getCurrentUser() {
        return currentUser;
    }

    private String normalize(String login) {
        return login.trim().toLowerCase(Locale.ROOT);
    }
}
