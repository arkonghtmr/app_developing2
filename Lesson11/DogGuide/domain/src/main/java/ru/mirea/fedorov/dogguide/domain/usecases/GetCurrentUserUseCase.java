package ru.mirea.fedorov.dogguide.domain.usecases;

import ru.mirea.fedorov.dogguide.domain.models.User;
import ru.mirea.fedorov.dogguide.domain.repository.UserRepository;

public class GetCurrentUserUseCase {
    private final UserRepository userRepository;

    public GetCurrentUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User execute() {
        return userRepository.getCurrentUser();
    }
}
