package ru.mirea.fedorov.dogguide.domain.usecases;

import ru.mirea.fedorov.dogguide.domain.models.Breed;
import ru.mirea.fedorov.dogguide.domain.models.User;
import ru.mirea.fedorov.dogguide.domain.repository.FavoriteRepository;
import ru.mirea.fedorov.dogguide.domain.repository.UserRepository;

public class AddBreedToFavoritesUseCase {
    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;

    public AddBreedToFavoritesUseCase(
            FavoriteRepository favoriteRepository,
            UserRepository userRepository
    ) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
    }

    public boolean execute(Breed breed) {
        User user = userRepository.getCurrentUser();
        if (user == null || breed == null) {
            return false;
        }
        return favoriteRepository.add(user.getLogin(), breed);
    }
}
