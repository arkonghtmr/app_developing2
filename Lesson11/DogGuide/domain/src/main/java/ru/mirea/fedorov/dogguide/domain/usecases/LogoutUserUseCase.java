package ru.mirea.fedorov.dogguide.domain.usecases;

import ru.mirea.fedorov.dogguide.domain.repository.UserRepository;

public class LogoutUserUseCase {
    private final UserRepository userRepository;

    public LogoutUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute() {
        userRepository.logout();
    }
}
