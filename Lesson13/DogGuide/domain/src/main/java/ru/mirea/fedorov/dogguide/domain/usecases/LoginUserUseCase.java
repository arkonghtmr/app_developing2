package ru.mirea.fedorov.dogguide.domain.usecases;

import ru.mirea.fedorov.dogguide.domain.models.AuthOutcome;
import ru.mirea.fedorov.dogguide.domain.repository.UserRepository;

public class LoginUserUseCase {
    private final UserRepository userRepository;

    public LoginUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AuthOutcome execute(String login, String password) {
        if (login == null || login.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            return AuthOutcome.failure("Введите логин и пароль");
        }
        return userRepository.login(login.trim(), password);
    }
}
