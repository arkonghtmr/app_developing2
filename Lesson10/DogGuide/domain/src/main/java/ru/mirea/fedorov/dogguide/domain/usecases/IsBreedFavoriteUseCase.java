package ru.mirea.fedorov.dogguide.domain.usecases;

import ru.mirea.fedorov.dogguide.domain.models.User;
import ru.mirea.fedorov.dogguide.domain.repository.FavoriteRepository;
import ru.mirea.fedorov.dogguide.domain.repository.UserRepository;

public class IsBreedFavoriteUseCase {
    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;

    public IsBreedFavoriteUseCase(
            FavoriteRepository favoriteRepository,
            UserRepository userRepository
    ) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
    }

    public boolean execute(String breedId) {
        User user = userRepository.getCurrentUser();
        if (user == null) {
            return false;
        }
        return favoriteRepository.isFavorite(user.getLogin(), breedId);
    }
}
