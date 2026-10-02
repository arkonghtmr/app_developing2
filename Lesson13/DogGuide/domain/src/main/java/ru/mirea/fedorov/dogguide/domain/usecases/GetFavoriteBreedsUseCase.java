package ru.mirea.fedorov.dogguide.domain.usecases;

import java.util.Collections;
import java.util.List;

import ru.mirea.fedorov.dogguide.domain.models.Breed;
import ru.mirea.fedorov.dogguide.domain.models.User;
import ru.mirea.fedorov.dogguide.domain.repository.FavoriteRepository;
import ru.mirea.fedorov.dogguide.domain.repository.UserRepository;

public class GetFavoriteBreedsUseCase {
    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;

    public GetFavoriteBreedsUseCase(
            FavoriteRepository favoriteRepository,
            UserRepository userRepository
    ) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
    }

    public List<Breed> execute() {
        User user = userRepository.getCurrentUser();
        if (user == null) {
            return Collections.emptyList();
        }
        return favoriteRepository.getAll(user.getLogin());
    }
}
