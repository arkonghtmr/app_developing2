package ru.mirea.fedorov.dogguide.domain.usecases;

import ru.mirea.fedorov.dogguide.domain.models.AuthOutcome;
import ru.mirea.fedorov.dogguide.domain.repository.UserRepository;

public class RegisterUserUseCase {
    private final UserRepository userRepository;

    public RegisterUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AuthOutcome execute(String login, String password) {
        if (login == null || login.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            return AuthOutcome.failure("Введите логин и пароль");
        }
        if (password.length() < 6) {
            return AuthOutcome.failure("Пароль не короче 6 символов");
        }
        return userRepository.register(login.trim(), password);
    }
}
