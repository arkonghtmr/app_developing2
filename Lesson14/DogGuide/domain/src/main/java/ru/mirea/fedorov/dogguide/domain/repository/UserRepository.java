package ru.mirea.fedorov.dogguide.domain.repository;

import ru.mirea.fedorov.dogguide.domain.models.AuthOutcome;
import ru.mirea.fedorov.dogguide.domain.models.User;

public interface UserRepository {
    AuthOutcome login(String login, String password);

    AuthOutcome register(String login, String password);

    void logout();

    User getCurrentUser();
}
