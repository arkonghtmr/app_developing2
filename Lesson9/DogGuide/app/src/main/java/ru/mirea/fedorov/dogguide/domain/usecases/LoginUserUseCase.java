package ru.mirea.fedorov.dogguide.domain.usecases;

import ru.mirea.fedorov.dogguide.domain.models.User;
import ru.mirea.fedorov.dogguide.domain.repository.UserRepository;

public class LoginUserUseCase {
    private final UserRepository userRepository;

    public LoginUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(String login, String password) {
        if (login == null || login.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            return null;
        }
        return userRepository.login(login.trim(), password);
    }
}
