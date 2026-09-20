package ru.mirea.fedorov.dogguide.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.mirea.fedorov.dogguide.domain.models.Breed;
import ru.mirea.fedorov.dogguide.domain.models.User;
import ru.mirea.fedorov.dogguide.domain.repository.BreedRepository;
import ru.mirea.fedorov.dogguide.domain.repository.FavoriteRepository;
import ru.mirea.fedorov.dogguide.domain.repository.UserRepository;
import ru.mirea.fedorov.dogguide.domain.usecases.GetBreedListUseCase;
import ru.mirea.fedorov.dogguide.domain.usecases.GetCurrentUserUseCase;
import ru.mirea.fedorov.dogguide.domain.usecases.GetFavoriteBreedsUseCase;
import ru.mirea.fedorov.dogguide.domain.usecases.LogoutUserUseCase;

public class BreedListViewModel extends ViewModel {
    private final GetBreedListUseCase getBreedListUseCase;
    private final GetFavoriteBreedsUseCase getFavoriteBreedsUseCase;
    private final GetCurrentUserUseCase getCurrentUserUseCase;
    private final LogoutUserUseCase logoutUserUseCase;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final MutableLiveData<List<Breed>> networkBreeds = new MutableLiveData<>();
    private final MutableLiveData<List<Breed>> databaseBreeds = new MutableLiveData<>();
    private final MediatorLiveData<List<Breed>> catalog = new MediatorLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<User> session = new MutableLiveData<>();

    public BreedListViewModel(
            BreedRepository breedRepository,
            FavoriteRepository favoriteRepository,
            UserRepository userRepository
    ) {
        this.getBreedListUseCase = new GetBreedListUseCase(breedRepository);
        this.getFavoriteBreedsUseCase = new GetFavoriteBreedsUseCase(
                favoriteRepository,
                userRepository
        );
        this.getCurrentUserUseCase = new GetCurrentUserUseCase(userRepository);
        this.logoutUserUseCase = new LogoutUserUseCase(userRepository);

        catalog.addSource(networkBreeds, value -> merge());
        catalog.addSource(databaseBreeds, value -> merge());
        refreshSession();
        load();
    }

    public LiveData<List<Breed>> getCatalog() {
        return catalog;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<User> getSession() {
        return session;
    }

    public void refreshSession() {
        session.setValue(getCurrentUserUseCase.execute());
        executor.execute(() -> databaseBreeds.postValue(getFavoriteBreedsUseCase.execute()));
    }

    public void logout() {
        logoutUserUseCase.execute();
        refreshSession();
    }

    public void load() {
        loading.setValue(true);
        executor.execute(() -> {
            List<Breed> fromNetwork = getBreedListUseCase.execute();
            List<Breed> fromDatabase = getFavoriteBreedsUseCase.execute();
            networkBreeds.postValue(fromNetwork);
            databaseBreeds.postValue(fromDatabase);
            loading.postValue(false);
        });
    }

    private void merge() {
        Map<String, Breed> merged = new LinkedHashMap<>();
        List<Breed> fromDatabase = databaseBreeds.getValue();
        List<Breed> fromNetwork = networkBreeds.getValue();
        if (fromDatabase != null) {
            for (Breed breed : fromDatabase) {
                merged.put(breed.getId(), breed);
            }
        }
        if (fromNetwork != null) {
            for (Breed breed : fromNetwork) {
                merged.put(breed.getId(), breed);
            }
        }
        catalog.setValue(new ArrayList<>(merged.values()));
    }

    @Override
    protected void onCleared() {
        executor.shutdownNow();
        super.onCleared();
    }
}
