package ru.mirea.fedorov.dogguide.domain.repository;

import ru.mirea.fedorov.dogguide.domain.models.User;

public interface UserRepository {
    User login(String login, String password);

    User register(String login, String password);

    void logout();

    User getCurrentUser();
}
