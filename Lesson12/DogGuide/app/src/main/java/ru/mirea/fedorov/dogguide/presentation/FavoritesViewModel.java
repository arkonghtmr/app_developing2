package ru.mirea.fedorov.dogguide.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.mirea.fedorov.dogguide.domain.models.Breed;
import ru.mirea.fedorov.dogguide.domain.repository.FavoriteRepository;
import ru.mirea.fedorov.dogguide.domain.repository.UserRepository;
import ru.mirea.fedorov.dogguide.domain.usecases.GetFavoriteBreedsUseCase;

public class FavoritesViewModel extends ViewModel {
    private final GetFavoriteBreedsUseCase getFavoriteBreedsUseCase;
    private final MutableLiveData<List<Breed>> favorites = new MutableLiveData<>();

    public FavoritesViewModel(
            FavoriteRepository favoriteRepository,
            UserRepository userRepository
    ) {
        this.getFavoriteBreedsUseCase = new GetFavoriteBreedsUseCase(
                favoriteRepository,
                userRepository
        );
    }

    public LiveData<List<Breed>> getFavorites() {
        return favorites;
    }

    public void load() {
        favorites.setValue(getFavoriteBreedsUseCase.execute());
    }
}
