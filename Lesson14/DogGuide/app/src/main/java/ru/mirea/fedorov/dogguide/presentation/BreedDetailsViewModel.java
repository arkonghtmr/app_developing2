package ru.mirea.fedorov.dogguide.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.mirea.fedorov.dogguide.domain.models.Breed;
import ru.mirea.fedorov.dogguide.domain.repository.BreedRepository;
import ru.mirea.fedorov.dogguide.domain.repository.FavoriteRepository;
import ru.mirea.fedorov.dogguide.domain.repository.UserRepository;
import ru.mirea.fedorov.dogguide.domain.usecases.AddBreedToFavoritesUseCase;
import ru.mirea.fedorov.dogguide.domain.usecases.GetBreedDetailsUseCase;
import ru.mirea.fedorov.dogguide.domain.usecases.GetCurrentUserUseCase;
import ru.mirea.fedorov.dogguide.domain.usecases.IsBreedFavoriteUseCase;
import ru.mirea.fedorov.dogguide.domain.usecases.RemoveBreedFromFavoritesUseCase;

public class BreedDetailsViewModel extends ViewModel {
    private final GetBreedDetailsUseCase getBreedDetailsUseCase;
    private final GetCurrentUserUseCase getCurrentUserUseCase;
    private final IsBreedFavoriteUseCase isBreedFavoriteUseCase;
    private final AddBreedToFavoritesUseCase addBreedToFavoritesUseCase;
    private final RemoveBreedFromFavoritesUseCase removeBreedFromFavoritesUseCase;

    private final MutableLiveData<Breed> breed = new MutableLiveData<>();
    private final MutableLiveData<Boolean> favorite = new MutableLiveData<>(false);
    private final MutableLiveData<String> message = new MutableLiveData<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public BreedDetailsViewModel(
            BreedRepository breedRepository,
            FavoriteRepository favoriteRepository,
            UserRepository userRepository
    ) {
        this.getBreedDetailsUseCase = new GetBreedDetailsUseCase(breedRepository);
        this.getCurrentUserUseCase = new GetCurrentUserUseCase(userRepository);
        this.isBreedFavoriteUseCase = new IsBreedFavoriteUseCase(
                favoriteRepository,
                userRepository
        );
        this.addBreedToFavoritesUseCase = new AddBreedToFavoritesUseCase(
                favoriteRepository,
                userRepository
        );
        this.removeBreedFromFavoritesUseCase = new RemoveBreedFromFavoritesUseCase(
                favoriteRepository,
                userRepository
        );
    }

    public LiveData<Breed> getBreed() {
        return breed;
    }

    public LiveData<Boolean> getFavorite() {
        return favorite;
    }

    public LiveData<String> getMessage() {
        return message;
    }

    public void load(String breedId) {
        executor.execute(() -> {
            Breed loaded = getBreedDetailsUseCase.execute(breedId);
            breed.postValue(loaded);
            if (loaded != null) {
                favorite.postValue(isBreedFavoriteUseCase.execute(loaded.getId()));
            }
        });
    }

    @Override
    protected void onCleared() {
        executor.shutdownNow();
        super.onCleared();
    }

    public void toggleFavorite() {
        Breed current = breed.getValue();
        if (current == null) {
            return;
        }
        if (getCurrentUserUseCase.execute() == null) {
            message.setValue("login_required");
            return;
        }
        if (Boolean.TRUE.equals(favorite.getValue())) {
            removeBreedFromFavoritesUseCase.execute(current.getId());
            favorite.setValue(false);
        } else {
            addBreedToFavoritesUseCase.execute(current);
            favorite.setValue(true);
        }
    }
}
